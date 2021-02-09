package com.example.assistantapplication;

public class Knowledge {
    private String question;
    private String answer;
    private String category1;
    private String category2;
    private String category3;
    private String landingUrl;
    private String imageInfo;
    private int faqno;

    public int getFaqno()   {return faqno; }
    public void setFaqno(int faqno)   {   this.faqno = faqno;  }

    public String getCategory3() {   return category3;   }
    public void setCategory3(String category3) { this.category3 = category3;  }

    public String getCategory2() {   return category2;   }
    public void setCategory2(String category2) { this.category2 = category2;  }

    public String getLandingUrl() {   return landingUrl;  }
    public void setLandingUrl(String landingUrl) {  this.landingUrl = landingUrl;  }

    public String getImageInfo() { return imageInfo;  }
    public void setImageInfo(String imageInfo) {  this.imageInfo = imageInfo;  }

    public String getCategory1() {  return category1;  }
    public void setCategory1(String category1) {  this.category1 = category1;   }

    public String getQuestion() { return question;    }
    public void setQuestion(String question) {     this.question = question;    }

    public String getAnswer() {   return answer;    }
    public void setAnswer(String answer) {    this.answer = answer;   }

    public Knowledge(String question, String answer, String category1, String category2, String category3, String landingUrl) {
        this.question = question;
        this.answer = answer;
        this.category1=category1;
        this.category2=category2;
        this.category3=category3;
        this.landingUrl=landingUrl;
        this.imageInfo=imageInfo;
    }

    public Knowledge(int faqno, String question, String answer, String category1, String category2, String category3, String landingUrl) {
        this.question = question;
        this.answer = answer;
        this.category1=category1;
        this.category2=category2;
        this.category3=category3;
        this.landingUrl=landingUrl;
        this.imageInfo=imageInfo;
        this.faqno = faqno;
    }
}
