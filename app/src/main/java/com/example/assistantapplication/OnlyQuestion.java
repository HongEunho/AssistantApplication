package com.example.assistantapplication;

public class OnlyQuestion implements Comparable<OnlyQuestion>{
    private String department;
    private String content;
    private String time;
    private int idx;

    public OnlyQuestion(String department, String content) {
        this.department = department;
        this.content = content;
    }

    public OnlyQuestion(int idx, String department, String content){
        this.idx = idx;
        this.department = department;
        this.content = content;
    }

    public OnlyQuestion(int idx, String department, String content, String time){
        this.idx = idx;
        this.department = department;
        this.content = content;
        this.time = time;
    }



    public int getIdx()   { return idx; }
    public void setIdx(int idx)   {   this.idx = idx;  }

    public String getDepartment() {   return department;   }
    public void setDepartment(String department) { this.department = department;  }

    public String getContent() {   return content;   }
    public void setContent(String content) { this.content = content;  }

    public String getTime() {   return time;  }
    public void setTime(String time) {  this.time = time;  }


    @Override
    public int compareTo(OnlyQuestion o) {
        return -(this.time.compareTo(o.time));

    }
}
