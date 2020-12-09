package com.naccoro.wask.customview.waskdialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.naccoro.wask.R;
import com.naccoro.wask.utils.MetricsUtil;

import java.util.List;

/**
 * WaskDialog 클래스
 * @author KIM SEONGGYU
 * @version 1.0.0
 * @since 2020.08.06
 */
public class WaskDialog extends DialogFragment {

    private static final String TAG = "WaskDialog";

    private String title;
    private boolean titleDivider;
    private String message;
    private int contentRes = -1;
    private List<WaskDialogButton> verticalButtons;
    private List<WaskDialogButton> horizontalButtons;

    private TextView textViewDialogTitle;
    private TextView textViewDialogMessage;
    private LinearLayout linearLayoutDialogContent;
    private LinearLayout linearLayoutDialogButtons;

    private View contentView;
    private ContentViewCallback contentCallback;

    public WaskDialog(String title, boolean titleDivider, String message, @LayoutRes int contentRes, ContentViewCallback contentCallback, List<WaskDialogButton> verticalButtons,
                      List<WaskDialogButton> horizontalButtons) {
        super();
        this.title = title;
        this.titleDivider = titleDivider;
        this.message = message;
        this.contentRes = contentRes;
        this.contentCallback = contentCallback;
        this.verticalButtons = verticalButtons;
        this.horizontalButtons = horizontalButtons;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_wask, container);

        //속성이 올바르게 적용되었는지 확인
        contentCheck();

        //뷰 초기화
        initView(view, inflater);

        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                //다이얼로그의 윤곽선을 둥글게 깎기
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.requestFeature(Window.FEATURE_NO_TITLE);
            } else {
                Log.e(TAG, "Window is null.");
            }
        } else {
            Log.e(TAG, "Dialog is null.");
        }

        return view;
    }

    private void initView(View view, LayoutInflater inflater) {
        textViewDialogTitle = view.findViewById(R.id.textview_dialog_title);
        textViewDialogMessage = view.findViewById(R.id.textview_dialog_message);
        linearLayoutDialogContent = view.findViewById(R.id.linearlayout_dialog_content);
        linearLayoutDialogButtons = view.findViewById(R.id.linearlayout_dialog_buttons);

        //타이틀과 메시지 초기화
        initTextView(textViewDialogTitle, title);
        initTextView(textViewDialogMessage, message);

        if (titleDivider) {
            view.findViewById(R.id.view_dialog_titledivider).setVisibility(View.VISIBLE);
        }

        //컨텐츠 적용
        if (contentRes != -1) {
            contentView = inflater.inflate(contentRes, linearLayoutDialogContent);

            //컨텐츠에 대한 콜백이 있다면 실행
            if (contentCallback != null) {
                contentCallback.onContentViewAttached(contentView);
            }
        } else { //컨텐츠가 없다면 약간의 마진을 줌
            ((ConstraintLayout.LayoutParams) linearLayoutDialogContent.getLayoutParams())
                    .setMargins(0, (int) MetricsUtil.convertDpToPixel(19, getContext()), 0, 0);
        }

        //세로 버튼 적용
        if (verticalButtons != null) {
            initButtons(verticalButtons, inflater, linearLayoutDialogButtons, LinearLayout.VERTICAL);
        }

        //가로 버튼 적용
        if (horizontalButtons != null) {
            //가로 버튼을 담을 하나의 LinearLayout을 생성
            LinearLayout verticalButtonGroup = new LinearLayout(getContext());
            verticalButtonGroup.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            //생성한 LinearLayout에 버튼들을 추가
            initButtons(horizontalButtons, inflater, verticalButtonGroup, LinearLayout.HORIZONTAL);

            //버튼이 추가된 LinearLayout을 다이얼로그에 추가
            linearLayoutDialogButtons.addView(verticalButtonGroup);
        }
    }

    private void initButtons(List<WaskDialogButton> buttons, LayoutInflater inflater, ViewGroup container, int orientation) {
        //각 버튼들에 대해 로직 수행
        for (WaskDialogButton waskDialogButton : buttons) {

            //버튼을 inflate하고 아직 붙이지는 않음
            View button = inflater.inflate(R.layout.item_dialog_button, container, false);

            //가로 버튼이라면 weight 값을 줘 균등하게 가로 길이를 가지도록 함
            if (orientation == LinearLayout.HORIZONTAL) {
                button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                //첫 번째 버튼이 아니라면
                if (waskDialogButton != buttons.get(0)) {
                    //구분선 삽입
                    setButtonDivider(container);
                }
            }

            //버튼에 문자열 적용
            ((TextView) button.findViewById(R.id.textview_dialogbuttonitem)).setText(waskDialogButton.getText());

            //버튼에 리스너 적용
            button.setOnClickListener((View v) -> waskDialogButton.getListener().onClick(this, contentView));

            //버튼 추가
            container.addView(button);
        }
    }

    private void setButtonDivider(ViewGroup container) {
        //뷰 하나 생성
        View divider = new View(getContext());

        //간격을 1로 하여 선처럼 보이게 함
        divider.setLayoutParams(new LinearLayout.LayoutParams((int) MetricsUtil.convertDpToPixel(1f, getContext()), LinearLayout.LayoutParams.MATCH_PARENT));

        //색 지정
        divider.setBackgroundResource(R.color.dividerGray);

        //삽입
        container.addView(divider);
    }

    private void initTextView(TextView textView, String text) {
        if (text != null) {
            textView.setText(text);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void contentCheck() {
        //메시지와 컨텐츠를 함께 사용하면 예외 발생
        if (message != null && contentRes != -1) {
            throw new IllegalArgumentException("Message cannot be used with ContentRes. Please choose one of the two.");
        }
    }

    public interface OnClickListener {
        void onClick(WaskDialog dialog, View view);
    }

    /**
     * 컨텐츠가 다이얼로그에 연결되고 실행되는 콜백을 정의하는 인터페이스
     * 연결된 컨텐츠를 inflate한 View를 파라미터로 가짐.
     */
    public interface ContentViewCallback {
        void onContentViewAttached(View view);
    }
}
