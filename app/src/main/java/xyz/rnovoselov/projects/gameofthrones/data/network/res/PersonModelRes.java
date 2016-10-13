package xyz.rnovoselov.projects.gameofthrones.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 14.10.16.
 */

public class PersonModelRes {

    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("culture")
    @Expose
    public String culture;
    @SerializedName("born")
    @Expose
    public String born;
    @SerializedName("died")
    @Expose
    public String died;
    @SerializedName("titles")
    @Expose
    public List<String> titles = new ArrayList<String>();
    @SerializedName("aliases")
    @Expose
    public List<String> aliases = new ArrayList<String>();
    @SerializedName("father")
    @Expose
    public String father;
    @SerializedName("mother")
    @Expose
    public String mother;
    @SerializedName("spouse")
    @Expose
    public String spouse;
    @SerializedName("allegiances")
    @Expose
    public List<String> allegiances = new ArrayList<String>();
    @SerializedName("books")
    @Expose
    public List<Object> books = new ArrayList<Object>();
    @SerializedName("povBooks")
    @Expose
    public List<String> povBooks = new ArrayList<String>();
    @SerializedName("tvSeries")
    @Expose
    public List<String> tvSeries = new ArrayList<String>();
    @SerializedName("playedBy")
    @Expose
    public List<String> playedBy = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getBorn() {
        return born;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public Long getFather() {
        return getPersonParentId(father);
    }

    public Long getMother() {
        return getPersonParentId(mother);
    }

    private Long getPersonParentId (String parent) {
        if (parent.isEmpty()) {
            return null;
        }
        String[] list = parent.split("/");
        return Long.valueOf(list[list.length - 1]);
    }
}