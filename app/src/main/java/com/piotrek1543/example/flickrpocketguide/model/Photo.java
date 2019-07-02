
package com.piotrek1543.example.flickrpocketguide.model;

import com.squareup.moshi.Json;

public class Photo {

    @Json(name = "id")
    private String id;
    @Json(name = "owner")
    private String owner;
    @Json(name = "secret")
    private String secret;
    @Json(name = "server")
    private String server;
    @Json(name = "farm")
    private Integer farm;
    @Json(name = "title")
    private String title;
    @Json(name = "ispublic")
    private Integer ispublic;
    @Json(name = "isfriend")
    private Integer isfriend;
    @Json(name = "isfamily")
    private Integer isfamily;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getFarm() {
        return farm;
    }

    public void setFarm(Integer farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIspublic() {
        return ispublic;
    }

    public void setIspublic(Integer ispublic) {
        this.ispublic = ispublic;
    }

    public Integer getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(Integer isfriend) {
        this.isfriend = isfriend;
    }

    public Integer getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(Integer isfamily) {
        this.isfamily = isfamily;
    }

}
