package com.example.nationalpetition.domain.board;

import lombok.Getter;

@Getter
public enum BoardCategory {

    POLITICS("정치개혁"), DIPLOMACY("외교/통일/국방"), JOB("일자리"), FUTURE("미래"), GROWTH("성장동력"), FARM("농산어촌"), HEALTH("보건복지"),
    BABY("육아/교육"), SAFE("안전/환경"), AGING("저출산/고령화대책"), ADMINISTRATION("행정"), PET("반려동물"), TRAFFIC("교통/건축/국토"),
    ECONOMY("경제민주화"), HUMAN("인권/성평등"), CULTURE("문화/예술/체육/언론"), ETC("기타");

    private final String category;

    BoardCategory(String category) {
        this.category = category;
    }

    public static BoardCategory getEnglishCategory(String category){
        for (BoardCategory boardCategory : BoardCategory.values()) {
            if (boardCategory.getCategory().equals(category)) {
                return boardCategory;
            }
        }
        return null;
    }

}
