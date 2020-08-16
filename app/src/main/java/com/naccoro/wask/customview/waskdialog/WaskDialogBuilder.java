package com.naccoro.wask.customview.waskdialog;

import androidx.annotation.LayoutRes;

import java.util.ArrayList;
import java.util.List;

/**
 * WaskDialog 빌더 클래스
 * @author KIM SEONGGYU
 * @version 1.0.0
 * @since 2020.08.06
 */
public class WaskDialogBuilder {

    private String title;
    private String message;
    private int contentRes = -1;
    private WaskDialog.ContentViewCallback contentCallback;
    private List<WaskDialogButton> verticalButtons;
    private List<WaskDialogButton> horizontalButtons;

    /**
     * 필요한 옵션을 모두 적용한 후 build() 하면 최종 WaskDialog 객체를 리턴.
     * @return 옵션이 적용된 WaskDialog 객체
     */
    public WaskDialog build() {
        return new WaskDialog(title, message, contentRes, contentCallback, verticalButtons, horizontalButtons);
    }

    /**
     * 다이얼로그의 타이틀을 적용
     * @param title 적용할 타이틀 문자열
     * @return 메서드 체이닝을 위한 자기 자신을 리턴
     */
    public WaskDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 다이얼로그의 메시지를 적용
     * contentRes와 함께 적용할 경우 예외가 발생
     * @param message 적용할 메시지 문자열
     * @return 메서드 체이닝을 위한 자기 자신을 리턴
     */
    public WaskDialogBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 다이얼로그의 컨텐츠를 적용
     * layout의 resourse를 입력하여야 함. (ex. R.layout.item)
     * message와 함께 적용할 경우 예외가 발생
     * @param contentRes 적용할 레이아웃
     * @return 메서드 체이닝을 위한 자기 자신을 리턴
     */
    public WaskDialogBuilder setContent(@LayoutRes int contentRes) {
        this.contentRes = contentRes;
        return this;
    }

    /**
     * 다이얼로그에 적용된 컨텐츠를 조작하기 위한 콜백 설정
     * @param callback 컨텐츠가 다이얼로그에 연결되면서 실행될 콜백
     * @return 메서드 체이닝을 위한 자기 자신을 리턴
     */
    public WaskDialogBuilder setContentCallback(WaskDialog.ContentViewCallback callback) {
        this.contentCallback = callback;
        return this;
    }

    /**
     * 세로로 배열되는 버튼을 추가
     * @param buttonText 버튼에 들어갈 문자열
     * @param listener 버튼을 눌렀을 때 실행될 리스너
     * @return 메서드 체이닝을 위한 자기 자신을 리턴
     */
    public WaskDialogBuilder addVerticalButton(String buttonText, WaskDialog.OnClickListener listener) {
        if (verticalButtons == null) {
            verticalButtons = new ArrayList<>();
        }
        verticalButtons.add(new WaskDialogButton(buttonText, listener));
        return this;
    }

    /**
     * 세로로 배열되는 버튼을 직접 추가
     * @param button 버튼 문구와 리스너를 담은 WaskDialogButton 객체
     * @return 메서드 체이닝을 위한 자기 자신을 리턴
     */
    public WaskDialogBuilder addVerticalButton(WaskDialogButton button) {
        verticalButtons.add(button);
        return this;
    }

    /**
     * 가로로 배열되는 버튼을 추가
     * @param buttonText 버튼에 들어갈 문자열
     * @param listener 버튼을 눌렀을 때 실행될 리스너
     * @return 메서드 체이닝을 위한 자기 자신을 리턴
     */
    public WaskDialogBuilder addHorizontalButton(String buttonText, WaskDialog.OnClickListener listener) {
        if (horizontalButtons == null) {
            horizontalButtons = new ArrayList<>();
        }
        horizontalButtons.add(new WaskDialogButton(buttonText, listener));
        return this;
    }

    /**
     * 가로로 배열되는 버튼을 직접 추가
     * @param button 버튼 문구와 리스너를 담은 WaskDialogButton 객체
     * @return 메서드 체이닝을 위한 자기 자신을 리턴
     */
    public WaskDialogBuilder addHorizontalButton(WaskDialogButton button) {
        horizontalButtons.add(button);
        return this;
    }
}
