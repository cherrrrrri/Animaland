package com.example.animaland.School;


public enum Language {
    ENGLISH(1),FRENCH(2),JAPANESE(3),OTHERS(4),ALL(5);

    private int code;
    private String other;

    Language(int i) {
        code=i;
    }

    public int getCode() {
        return code;
    }

    public void setOther(String other){
        this.other=other;
    }

    public String getText(){
        switch (getCode()){
            case 1:
                return "英语";
            case 2:
                return "法语";
            case 3:
                return "日语";
            case 4:
                return other;
            default:
                return "";
        }
    }

}

