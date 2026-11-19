package org.example.cocktailcraft.interface_adapter.click;

import org.example.cocktailcraft.use_case.click.ClickOutputBoundary;
import org.example.cocktailcraft.use_case.click.ClickOutputData;

public class ClickPresenter implements ClickOutputBoundary {
    public ClickPresenter(){

    }

    public void prepareSuccessView(ClickOutputData outputData){

    }

    public void prepareFailView(String errorMessage){

    }
}
