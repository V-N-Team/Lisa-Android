package ht.lisa.app.model;

import java.util.ArrayList;

import ht.lisa.app.model.response.BaseNewDrawResponse;
import ht.lisa.app.model.response.NewBoletDrawResponse;
import ht.lisa.app.model.response.NewBolotoDrawResponse;
import ht.lisa.app.model.response.NewLotto3DrawResponse;
import ht.lisa.app.model.response.NewLotto4DrawResponse;
import ht.lisa.app.model.response.NewLotto5DrawResponse;
import ht.lisa.app.model.response.NewLotto5jrDrawResponse;
import ht.lisa.app.util.TextUtil;

public class AllDrawDetails {
    private NewBolotoDrawResponse bolotoDrawResponse;
    private NewBoletDrawResponse boletDrawResponse;
    //    private NewMaryajDrawResponse maryajDrawResponse;
    private NewLotto3DrawResponse lotto3DrawResponse;
    private NewLotto4DrawResponse lotto4DrawResponse;
    private NewLotto5DrawResponse lotto5DrawResponse;
    private NewLotto5jrDrawResponse lotto5jrDrawResponse;

    public AllDrawDetails(NewBolotoDrawResponse bolotoDrawResponse, NewBoletDrawResponse boletDrawResponse, /*NewMaryajDrawResponse maryajDrawResponse, */NewLotto3DrawResponse lotto3DrawResponse, NewLotto4DrawResponse lotto4DrawResponse, NewLotto5DrawResponse lotto5DrawResponse, NewLotto5jrDrawResponse lotto5jrDrawResponse) {
        this.bolotoDrawResponse = bolotoDrawResponse;
        this.boletDrawResponse = boletDrawResponse;
        // this.maryajDrawResponse = maryajDrawResponse;
        this.lotto3DrawResponse = lotto3DrawResponse;
        this.lotto4DrawResponse = lotto4DrawResponse;
        this.lotto5DrawResponse = lotto5DrawResponse;
        this.lotto5jrDrawResponse = lotto5jrDrawResponse;
    }

    public ArrayList<BaseNewDrawResponse> getDraws() {
        ArrayList<BaseNewDrawResponse> draws = new ArrayList<>();
        setLottoNames();
        draws.add(bolotoDrawResponse);
        draws.add(boletDrawResponse);
        draws.add(lotto3DrawResponse);
        draws.add(lotto4DrawResponse);
        draws.add(lotto5DrawResponse);
        draws.add(lotto5jrDrawResponse);
        return draws;
    }

    public NewBolotoDrawResponse getBolotoDrawResponse() {
        return bolotoDrawResponse;
    }

    private void setLottoNames() {
        boletDrawResponse.setName(TextUtil.capitalize(Draw.BOLET));
        bolotoDrawResponse.setName(TextUtil.capitalize(Draw.BOLOTO));
        lotto3DrawResponse.setName(TextUtil.capitalize(Draw.LOTTO3));
        lotto4DrawResponse.setName(TextUtil.capitalize(Draw.LOTTO4));
        lotto5DrawResponse.setName(TextUtil.capitalize(Draw.LOTTO5));
        lotto5jrDrawResponse.setName(TextUtil.capitalize(Draw.LOTTO5JR));
    }
   /* public NewMaryajDrawResponse getMaryajDrawResponse() {
        return maryajDrawResponse;
    }*/
}
