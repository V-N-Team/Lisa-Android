package ht.lisa.app.model.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ht.lisa.app.model.City;

public class CitiesResponse implements Serializable {

    List<City> cities;

    public CitiesResponse(List<City> cities) {
        this.cities = cities;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public ArrayList<String> getCityNameList() {
        ArrayList<String> cityNameList = new ArrayList<>();
        for (City city : cities) {
            cityNameList.add(city.getCity());
        }
        return cityNameList;
    }

    public String getCityById(int id) {
        for (City city : cities) {
            if (id == city.getId()) {
                return city.getCity();
            }
        }
        return "-";
    }
}
