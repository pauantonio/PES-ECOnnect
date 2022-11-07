package com.econnect.client.ItemDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.econnect.API.ProductService.ProductDetails.Question;
import com.econnect.API.Translate.TranslateService;
import com.econnect.API.Translate.TranslateService.Translation;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.PopupMessage;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

public class QuestionListAdapter extends BaseAdapter {

    private Question[] _questions;
    private final ProductDetailsFragment _fragment;
    private final int _highlightColor;
    private static LayoutInflater inflater = null;

    public QuestionListAdapter(ProductDetailsFragment owner, Question[] questions, int highlightColor) {
        this._questions = questions;
        this._fragment = owner;
        this._highlightColor = highlightColor;
        inflater = (LayoutInflater) owner.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void replaceData(Question[] questions) {
        _questions = questions;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return _questions.length;
    }

    @Override
    public Question getItem(int i) {
        return _questions[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Initialize view and product
        final View vi = convertView != null ? convertView : inflater.inflate(R.layout.question_list_item, null);
        final Question q = _questions[position];
        int totalVotes = q.num_no + q.num_yes;
        int percentVotes;
        if (totalVotes > 0) percentVotes = (100 * q.num_yes) / totalVotes;
        else percentVotes = -1;

        if (q.translatedText == null) {
            // Translations not loaded: Set original question text and get translation
            setText(vi, q.text, totalVotes);

            // Attempt to translate question text
            ExecutionThread.nonUI(()->{
                try {
                    Translation t = new TranslateService().translateToCurrentLang(_fragment.requireContext(), q.text);
                    // Store translation
                    q.translatedText = t.translatedText;
                    ExecutionThread.UI(_fragment, ()-> setText(vi, t.translatedText, totalVotes));
                }
                catch (Exception e) {
                    q.translatedText = q.text;
                    ExecutionThread.UI(_fragment, ()-> {
                        PopupMessage.showToast(_fragment, Translate.id(R.string.could_not_translate));
                    });
                }
            });
        }
        else {
            // Translations already loaded
            setText(vi, q.translatedText, totalVotes);
        }

        // Set percent
        ProgressBar bar = vi.findViewById(R.id.questionPercentBar);
        TextView yesPercent = vi.findViewById(R.id.yesPercentText);
        TextView noPercent = vi.findViewById(R.id.noPercentText);

        if (percentVotes != -1) {
            bar.setProgress(percentVotes);
            yesPercent.setText(percentVotes + "%");
            noPercent.setText(100-percentVotes + "%");
        } else {
            bar.setProgress(50);
            yesPercent.setText("");
            noPercent.setText("");
        }

        ImageButton voteYesButton = vi.findViewById(R.id.voteYesButton);
        ImageButton voteNoButton = vi.findViewById(R.id.voteNoButton);

        // Highlight previous user choice and set listeners
        setVoteButtonListener(q.questionid, voteYesButton, IDetailsController.QuestionAnswer.yes);
        setVoteButtonListener(q.questionid, voteNoButton, IDetailsController.QuestionAnswer.no);
        switch (q.user_answer) {
            case "yes":
                voteYesButton.setColorFilter(_highlightColor);
                voteNoButton.setColorFilter(null);
                setVoteButtonListener(q.questionid, voteYesButton, IDetailsController.QuestionAnswer.none);
                break;
            case "no":
                voteYesButton.setColorFilter(null);
                voteNoButton.setColorFilter(_highlightColor);
                setVoteButtonListener(q.questionid, voteNoButton, IDetailsController.QuestionAnswer.none);
                break;
            case "none":
                voteYesButton.setColorFilter(null);
                voteNoButton.setColorFilter(null);
                break;
            default:
                throw new RuntimeException("Invalid user answer: " + q.user_answer);
        }

        return vi;
    }

    private void setText(View vi, String text, int totalVotes) {
        TextView questionText = vi.findViewById(R.id.questionText);
        String displayedText;
        if (totalVotes != 1) displayedText = Translate.id(R.string.question_text_and_votes, text, totalVotes);
        else displayedText = Translate.id(R.string.question_text_and_votes_one, text, totalVotes);
        questionText.setText(displayedText);
    }

    private void setVoteButtonListener(int id, ImageButton voteButton, IDetailsController.QuestionAnswer answer) {
        voteButton.setOnClickListener(view -> _fragment.question(id, answer));
    }
}
