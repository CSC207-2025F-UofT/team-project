package view;

import adapters.viewmodel.GameViewModel;
import adapters.viewmodel.GameViewModel.CardViewData;
import adapters.viewmodel.GameViewModel.TileViewData;
import view.GameArtwork.Portrait;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiConsumer;

final class BoardPanel extends JPanel {
    private static final int SIZE = GameViewModel.BOARD_SIZE;
    static final int NO_HOVERED_SPELL = -1;
    // The centers of the outer grid lines in the shipped illustration are inset by 1%.
    private static final double ART_GRID_INSET = 0.01;

    private final TileButton[][] tiles = new TileButton[SIZE][SIZE];
    private final JPanel grid = new JPanel(new GridLayout(SIZE, SIZE));
    private final JLayeredPane boardSurface = new JLayeredPane();
    private final BoardEffects effects = new BoardEffects(point -> tiles[point.y][point.x].getBounds());
    private BufferedImage artwork = GameArtwork.boardBackground();
    private double gridInset = artwork == null ? 0 : ART_GRID_INSET;

    BoardPanel(BiConsumer<Point, Integer> showTile) {
        setLayout(null);
        setOpaque(false);
        grid.setOpaque(false);
        boardSurface.add(grid, JLayeredPane.DEFAULT_LAYER);
        boardSurface.add(effects, JLayeredPane.PALETTE_LAYER);
        add(boardSurface);

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Point position = new Point(column, row);
                TileButton tile = new TileButton(position, showTile);
                tiles[row][column] = tile;
                grid.add(tile);
            }
        }
    }

    void showBoard(GameViewModel model, GameArtwork images) {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                tiles[row][column].showContents(model.tileAt(column, row), images);
            }
        }
    }

    OptionalInt spellUnderPointer(Point position) {
        TileButton tile = tiles[position.y][position.x];
        return tile.pointer == null ? OptionalInt.empty() : OptionalInt.of(tile.spellAt(tile.pointer));
    }

    BoardEffects effects() {
        return effects;
    }

    void setHighlightedTiles(Collection<Point> positions) {
        boolean[] highlighted = new boolean[SIZE * SIZE];
        for (Point position : positions) {
            highlighted[GameViewModel.tileIndex(position.x, position.y)] = true;
        }
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                tiles[row][column].setRangeHighlighted(highlighted[GameViewModel.tileIndex(column, row)]);
            }
        }
    }

    void setArtwork(BufferedImage image) {
        artwork = image;
        gridInset = 0;
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(550, 550);
    }

    @Override
    public void doLayout() {
        int side = Math.min(getWidth(), getHeight());
        int left = (getWidth() - side) / 2;
        int top = (getHeight() - side) / 2;
        int inset = (int) Math.round(side * gridInset);
        int gridSide = side - 2 * inset;
        boardSurface.setBounds(left + inset, top + inset, gridSide, gridSide);
        grid.setBounds(0, 0, gridSide, gridSide);
        effects.setBounds(0, 0, gridSide, gridSide);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (artwork == null) {
            return;
        }
        Graphics2D painter = (Graphics2D) graphics.create();
        painter.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int side = Math.min(getWidth(), getHeight());
        int left = (getWidth() - side) / 2;
        int top = (getHeight() - side) / 2;
        painter.clipRect(left, top, side, side);
        double scale = (double) side / Math.min(artwork.getWidth(), artwork.getHeight());
        int width = (int) Math.ceil(artwork.getWidth() * scale);
        int height = (int) Math.ceil(artwork.getHeight() * scale);
        painter.drawImage(artwork, left + (side - width) / 2, top + (side - height) / 2, width, height, null);
        painter.dispose();
    }

    private final class TileButton extends JButton {
        private final String coordinate;
        private final Color fallback;
        private Portrait occupant;
        private List<Portrait> spells = List.of();
        private boolean opponentControlled;
        private boolean rangeHighlighted;
        private Point pointer;

        TileButton(Point position, BiConsumer<Point, Integer> showTile) {
            coordinate = String.valueOf((char) ('A' + position.x)) + (position.y + 1);
            fallback = (position.x + position.y) % 2 == 0 ? GameViewStyle.TILE_LIGHT : GameViewStyle.TILE_DARK;
            GameViewStyle.configureButton(this);
            setBorderPainted(false);
            setName("tile-" + coordinate);
            MouseAdapter inspectCard = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent event) {
                    mouseMoved(event);
                }

                @Override
                public void mouseMoved(MouseEvent event) {
                    pointer = event.getPoint();
                    showTile.accept(position, spellAt(pointer));
                }

                @Override
                public void mouseExited(MouseEvent event) {
                    pointer = null;
                }
            };
            addMouseListener(inspectCard);
            addMouseMotionListener(inspectCard);
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent event) {
                    showTile.accept(position, pointer == null ? NO_HOVERED_SPELL : spellAt(pointer));
                }
            });
            addActionListener(event -> {
                // TODO: Forward the clicked column and row to the tile selection controller.
            });
        }

        void showContents(TileViewData tile, GameArtwork images) {
            CardViewData card = tile.occupant();
            occupant = card == null ? null : images.card(card);
            spells = tile.spells().stream().map(images::card).toList();
            opponentControlled = tile.opponentControlled();
            String details = coordinate;
            if (card != null) {
                details += " · " + card.accessibleDescription();
            } else if (tile.isEmpty()) {
                details += " · Empty";
            }
            if (opponentControlled) {
                details += " · Opponent controlled";
            }
            if (!spells.isEmpty()) {
                details += " · " + spells.size() + (spells.size() == 1 ? " spell equipped" : " spells equipped");
            }
            getAccessibleContext().setAccessibleName(details);
            setToolTipText(occupant != null && occupant.image() == null ? card.name() : null);
            repaint();
        }

        void setRangeHighlighted(boolean highlighted) {
            if (rangeHighlighted != highlighted) {
                rangeHighlighted = highlighted;
                getAccessibleContext().setAccessibleDescription(highlighted ? "Highlighted tile" : null);
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            if (artwork == null) {
                graphics.setColor(fallback);
                graphics.fillRect(0, 0, getWidth(), getHeight());
            }
            if (rangeHighlighted) {
                graphics.setColor(GameViewStyle.RANGE_HIGHLIGHT);
                graphics.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
                graphics.setColor(GameViewStyle.RANGE_OUTLINE);
                graphics.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
            }
            boolean highlighted = getModel().isRollover() || hasFocus();
            if (highlighted) {
                graphics.setColor(GameViewStyle.TILE_HIGHLIGHT);
                graphics.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
            }
            if (occupant != null || !spells.isEmpty()) {
                paintCardStack(graphics);
            }
            if (highlighted) {
                graphics.setColor(GameViewStyle.GOLD);
                graphics.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
            }
        }

        private void paintCardStack(Graphics graphics) {
            StackLayout layout = stackLayout();
            if (layout.height() <= 0) {
                return;
            }
            Graphics2D painter = (Graphics2D) graphics.create();
            if (opponentControlled) {
                painter.rotate(Math.PI, getWidth() / 2.0, getHeight() / 2.0);
            }
            for (int index = spells.size() - 1; index >= 0; index--) {
                paintCard(painter, spells.get(index), layout.bounds(spellLayer(index)));
            }
            if (occupant != null) {
                paintCard(painter, occupant, layout.bounds(0));
            }
            painter.dispose();
        }

        private StackLayout stackLayout() {
            int side = Math.min(getWidth(), getHeight());
            int padding = Math.max(2, side / 14);
            int layers = spells.size() + (occupant == null ? 0 : 1);
            int spread = Math.round(side * Math.min(0.22f, 0.14f * (layers - 1)));
            int height = side - 2 * padding - spread;
            int width = Portrait.cardWidth(height);
            int left = (getWidth() - width - spread) / 2;
            int top = (getHeight() - height - spread) / 2;
            float step = spread / (float) Math.max(1, layers - 1);
            return new StackLayout(left, top, width, height, step);
        }

        private int spellAt(Point point) {
            if (spells.isEmpty()) {
                return NO_HOVERED_SPELL;
            }
            StackLayout layout = stackLayout();
            if (layout.height() <= 0) {
                return NO_HOVERED_SPELL;
            }
            Point upright = opponentControlled
                    ? new Point(getWidth() - 1 - point.x, getHeight() - 1 - point.y)
                    : point;
            // Test front to back so covered parts of a spell cannot take focus.
            if (occupant != null && layout.covers(0, upright)) {
                return NO_HOVERED_SPELL;
            }
            for (int index = 0; index < spells.size(); index++) {
                if (layout.covers(spellLayer(index), upright)) {
                    return index;
                }
            }
            return NO_HOVERED_SPELL;
        }

        private int spellLayer(int index) {
            return index + (occupant == null ? 0 : 1);
        }

        private void paintCard(Graphics2D painter, Portrait portrait, Rectangle bounds) {
            portrait.paint(painter, bounds.x, bounds.y, bounds.width, bounds.height);
            painter.setColor(GameViewStyle.BOARD_CARD_OUTLINE);
            painter.drawRect(bounds.x - 1, bounds.y - 1, bounds.width + 1, bounds.height + 1);
        }
    }

    private record StackLayout(int left, int top, int width, int height, float step) {
        Rectangle bounds(int layer) {
            int offset = Math.round(step * layer);
            return new Rectangle(left + offset, top + offset, width, height);
        }

        boolean covers(int layer, Point point) {
            Rectangle card = bounds(layer);
            card.grow(1, 1); // Include the visible border drawn around each card.
            return card.contains(point);
        }
    }
}
