package com.example.smqpr.a2hi_tech;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Post {

    private String heading;
    private String text;
    private String urlPhoto;
    private String link;
    private String tag;

   /* public Post(String heading, String text , String link) {
        this.heading = heading;
        this.text = text;
        this.link = link;
    }*/

    public Post(String heading, String link, String tag) {
        this.heading = heading;
        this.link = link;
        this.tag = tag;
    }
}