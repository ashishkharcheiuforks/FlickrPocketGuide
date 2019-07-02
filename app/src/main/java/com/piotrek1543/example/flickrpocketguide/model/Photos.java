
package com.piotrek1543.example.flickrpocketguide.model;

import java.util.List;
import com.squareup.moshi.Json;

public class Photos {

    @Json(name = "page")
    private Integer page;
    @Json(name = "pages")
    private Integer pages;
    @Json(name = "perpage")
    private Integer perpage;
    @Json(name = "total")
    private String total;
    @Json(name = "photo")
    private List<Photo> photo = null;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPerpage() {
        return perpage;
    }

    public void setPerpage(Integer perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

}
