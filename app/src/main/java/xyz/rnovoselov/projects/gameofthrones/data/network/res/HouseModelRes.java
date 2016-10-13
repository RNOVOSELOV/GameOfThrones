package xyz.rnovoselov.projects.gameofthrones.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by novoselov on 13.10.2016.
 */

public class HouseModelRes {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("coatOfArms")
    @Expose
    private String coatOfArms;
    @SerializedName("words")
    @Expose
    private String words;
    @SerializedName("titles")
    @Expose
    private List<String> titles = new ArrayList<String>();
    @SerializedName("seats")
    @Expose
    private List<String> seats = new ArrayList<String>();
    @SerializedName("currentLord")
    @Expose
    private String currentLord;
    @SerializedName("heir")
    @Expose
    private String heir;
    @SerializedName("overlord")
    @Expose
    private String overlord;
    @SerializedName("founded")
    @Expose
    private String founded;
    @SerializedName("founder")
    @Expose
    private String founder;
    @SerializedName("diedOut")
    @Expose
    private String diedOut;
    @SerializedName("ancestralWeapons")
    @Expose
    private List<String> ancestralWeapons = new ArrayList<String>();
    @SerializedName("cadetBranches")
    @Expose
    private List<String> cadetBranches = new ArrayList<String>();
    @SerializedName("swornMembers")
    @Expose
    private List<String> swornMembers = new ArrayList<String>();

    public String getHouseName() {
        return name;
    }

    public void setHouseName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HouseModelRes{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", words='" + words + '\'' +
                '}';
    }
}