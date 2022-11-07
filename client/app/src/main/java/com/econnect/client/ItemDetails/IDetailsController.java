package com.econnect.client.ItemDetails;

public interface IDetailsController {

    enum QuestionAnswer {
        yes,
        no,
        none
    }

    void updateUIElements();

    void reviewProduct();

    void setStars(int i);

    void answerQuestion(int questionId, QuestionAnswer answer);

    int getPreviousReview();

}
