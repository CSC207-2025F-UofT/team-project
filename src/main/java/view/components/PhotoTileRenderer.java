package view.components;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import interface_adapter.manipulate_trips.PhotoTile;

/** Draws one photo as a square thumbnail with its time underneath. */
public class PhotoTileRenderer extends JLabel implements ListCellRenderer<PhotoTile> {
    private final ThumbnailCache thumbnails;
    private final Runnable repaintList;

    public PhotoTileRenderer(ThumbnailCache thumbnails, Runnable repaintList) {
        this.thumbnails = thumbnails;
        this.repaintList = repaintList;
        setOpaque(true);   // JLabel is transparent by default, so the selection colour wouldn't show
        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setIconTextGap(4);
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends PhotoTile> list, PhotoTile tile,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        setIcon(thumbnails.get(tile.getPhotoId(), repaintList));
        setText(tile.getCaption());
        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
        return this;
    }
}