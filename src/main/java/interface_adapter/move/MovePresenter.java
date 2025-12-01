package interface_adapter.move;

import API.MoveStaticMapInterface;
import entity.Monster;
import entity.User;
import interface_adapter.Battle.Battle_State;
import interface_adapter.Battle.Battle_ViewModel;
import interface_adapter.ViewManagerModel;
import use_case.move.MoveOutputBoundary;
import use_case.move.MoveOutputData;

import javax.swing.*;

public class MovePresenter implements MoveOutputBoundary {

    private final MoveViewModel moveViewModel;
    private final MoveStaticMapInterface staticMapService;
    private final Battle_ViewModel battleViewModel;
    private final ViewManagerModel viewManagerModel;


    public MovePresenter(ViewManagerModel viewManagerModel, MoveViewModel moveViewModel, MoveStaticMapInterface staticMapService, Battle_ViewModel battleViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.moveViewModel = moveViewModel;
        this.staticMapService = staticMapService;
        this.battleViewModel = battleViewModel;
    }

    @Override

    public void present(MoveOutputData moveOutputData) {
        MoveState moveState = moveViewModel.getState();

        moveState.setLeftButtonEnabled(moveOutputData.isCanMoveLeft());
        moveState.setRightButtonEnabled(moveOutputData.isCanMoveRight());
        moveState.setCurrentLocationName(moveOutputData.getCurrentLocationName());
        moveState.setMonster(moveOutputData.getMonster());
//        state.setItem(outputData.getItem());
        String linearMap = formatLinearMap(
                moveOutputData.getCurrentIndex(),
                moveOutputData.getMapSize()
        );
        moveState.setLinearMap(linearMap);

        ImageIcon mapImage = staticMapService.getMapImage(
                moveOutputData.getLatitude(),
                moveOutputData.getLongitude()
        );
        moveState.setStaticMapImage(mapImage);

        moveViewModel.firePropertyChange();
    }

    @Override
    public void switchToBattleView(User user, Monster monster) {
        Battle_State battleState = battleViewModel.getState();

        battleState.setUser(user);
        battleState.setMonster(monster);

        battleViewModel.firePropertyChange();

        viewManagerModel.setState(battleViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    private String formatLinearMap(int currentIndex, int mapSize) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mapSize; i++) {
            if (i == currentIndex) {
                sb.append("■");
            } else {
                sb.append("□");
            }

            if (i < mapSize - 1) {
                sb.append(" — ");
            }
        }
        return sb.toString();
    }
}
