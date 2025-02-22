package team9.fft.view.controllers;

import javafx.scene.layout.Region;
import javafx.util.Builder;
import team9.fft.view.builders.BackStatementHeader;
import team9.fft.view.builders.BankStatementUploadView;

public class BankStatementHeaderController {
    public Builder<Region> builder;

    public BankStatementHeaderController(){
        this.builder = new BackStatementHeader();
    }

    public Region getView(){
        return builder.build();
    }
}