package com.example.assistantapplication;

public class Knowledge {
    private String question;
    private String answer;
    private String category1;
    private String category2;
    private String landingUrl;
    private String imageInfo;

    public String getCategory2() {   return category2;   }
    public void setCategory2(String category2) { this.category2 = category2;  }

    public String getLandingUrl() {   return landingUrl;  }
    public void setLandingUrl(String landingUrl) {  this.landingUrl = landingUrl;  }

    public String getImageInfo() { return imageInfo;  }
    public void setImageInfo(String imageInfo) {  this.imageInfo = imageInfo;  }

    public String getCategory1() {  return category1;  }
    public void setCategory1(String category1) {  this.category1 = category1;   }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Knowledge(String question, String answer, String category1, String category2, String landingUrl, String imageInfo) {
        this.question = question;
        this.answer = answer;
        this.category1=category1;
        this.category2=category2;
        this.landingUrl=landingUrl;
        this.imageInfo=imageInfo;

    }
}
