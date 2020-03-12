package com.yizhitong.wisdombuilding.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhitong.wisdombuilding.R;
import com.yizhitong.wisdombuilding.utils.MyUtils;


/**
 * 通用的显示label和文本的View
 */
public class LabelEditRow extends LinearLayout {


    private static final int INVALID = -1;

    private EditText value;

    private TextView label;

    private CheckBox pwdCheck;

    private TextView unit;

    private int dividerIndent; // 分隔线缩进值

    private boolean drawBottomDivider = true;
    private boolean drawTopDivider = false;
    private boolean labelValueRight = false;
    private Paint paint;


    public LabelEditRow(Context context) {
        this(context, null);
    }
    public LabelEditRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 设置布局文件
        View.inflate(context, R.layout.base_edit_row, this);
        // 初始化布局参数
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.label_row_height));
        setBackgroundColor(getResources().getColor(R.color.jx_main_fore));
        int padding = getResources().getDimensionPixelOffset(R.dimen.jx_content_padding);
        setPadding(padding, 0, padding, 0);
        // 初始化控件
        label =  findViewById(R.id.row_label);
        value =  findViewById(R.id.row_value);
        pwdCheck =  findViewById(R.id.pwdCheck);
        unit = findViewById(R.id.unit);
        pwdCheck.setVisibility(View.GONE);
        // xml属性为控件赋值
        if (attrs != null) {
            TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.LabelIconRow);

            CharSequence text = styled.getText(R.styleable.LabelIconRow_labelText);
            if (!TextUtils.isEmpty(text)) {
                label.setText(text);
                // 有设置文本时，保持最小宽度
                label.setMinWidth(getResources().getDimensionPixelSize(R.dimen.jx_label_edit_min_width));
            }

            int drawableLeft = styled.getResourceId(R.styleable.LabelIconRow_labelIcon, INVALID);
            if(drawableLeft != INVALID){
                label.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(drawableLeft),null,null,null);
            }

            int scaleLeft = styled.getResourceId(R.styleable.LabelIconRow_labelScaleIcon, INVALID);
            if(scaleLeft != INVALID){
                Drawable left = context.getResources().getDrawable(scaleLeft);
                final int size = MyUtils.dpToPx(context, 20);
                left.setBounds(0, 0, size, size);
                label.setCompoundDrawables(left, null, null, null);
            }

            int labelColor = styled.getColor(R.styleable.LabelIconRow_labelTextColor, INVALID);
            if(labelColor != INVALID) {
                label.setTextColor(labelColor);
            }

            int valueColor = styled.getColor(R.styleable.LabelIconRow_labelValueColor, INVALID);
            if(labelColor != INVALID) {
                value.setTextColor(valueColor);
            }
            
            int hintColor = styled.getColor(R.styleable.LabelIconRow_labelHintColor, INVALID);
            if(labelColor != INVALID) {
                value.setHintTextColor(hintColor);
            }

            // 字体大小
            float textSize = styled.getDimension(R.styleable.LabelIconRow_labelTextSize, INVALID);
            if(textSize != INVALID) {
                label.setTextSize(textSize);
                value.setTextSize(textSize);
            }

            textSize = styled.getDimension(R.styleable.LabelIconRow_labelValueSize, INVALID);
            if(textSize != INVALID){
                value.setTextSize(textSize);
            }
            labelValueRight = styled.getBoolean(R.styleable.LabelIconRow_labelValueRight,false);
            if (labelValueRight!=false){
                value.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            }
            CharSequence hint = styled.getText(R.styleable.LabelIconRow_labelHint);
            value.setHint(hint);

            int labelWidth = styled.getDimensionPixelSize(R.styleable.LabelIconRow_labelExactlyWidth, 0);
            if (labelWidth > 0) {
                label.setMinWidth(labelWidth);
            }

            dividerIndent = styled.getDimensionPixelSize(R.styleable.LabelIconRow_labelDividerIndent, 0);
            drawBottomDivider = styled.getBoolean(R.styleable.LabelIconRow_labelBottomDivider, true);
            drawTopDivider = styled.getBoolean(R.styleable.LabelIconRow_labelTopDivider, false);
            styled.recycle();

            // Edit相关的属性
            TypedArray editStyled = getContext().obtainStyledAttributes(attrs, R.styleable.LabelEditRow);

            // 输入类型
            int inputType = editStyled.getInt(R.styleable.LabelEditRow_android_inputType, EditorInfo.TYPE_CLASS_TEXT);
            value.setInputType(inputType);

            // 密码模式，显示密码按钮，并设置EditText的InputTpe
            if(editStyled.getBoolean(R.styleable.LabelEditRow_labelPasswordMode, false)) {
                value.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                if(inputType == EditorInfo.TYPE_CLASS_NUMBER){
                    value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                }
                pwdCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            value.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            value.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        // 输入框光标一直在输入文本后面
                        value.setSelection(value.getText().length());
                    }
                });
            }

            // 最大长度
            int maxLength = styled.getInt(R.styleable.LabelEditRow_android_maxLength, INVALID);
            if(maxLength != INVALID) {
                value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }

            // 单位显示
            CharSequence unitText = styled.getText(R.styleable.LabelIconRow_labelUnit);
            if (!TextUtils.isEmpty(unitText)) {
                unit.setText(unitText);
                unit.setVisibility(View.VISIBLE);
            }

            editStyled.recycle();

        }

        initPaint();

        // 不设置背景的情况下，继承自ViewGroup的容器不会调用onDraw方法，调用下面的方法使其调用onDraw()方法
        setWillNotDraw(!(drawBottomDivider || drawTopDivider));

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.text_dark_gray));
    }

    public void setText(String text) {
        value.setText(text);
        /*if (!TextUtils.isEmpty(text)) {
        }*/
    }
    public void setLabelText(String text) {
        label.setText(text);
        /*if (!TextUtils.isEmpty(text)) {
        }*/
    }
    public Editable getText() {
        return value.getText();
    }
    
    public void setHint(String hint) {
        value.setHint(hint);
    }


    public void setInputType(int type) {
        value.setInputType(type);
    }

    public EditText getEditText() {
        return value;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        value.setEnabled(enabled);
    }

    //设置密码显示
    public void setPassWordVisiable(boolean visiable){
        if (visiable) {
            value.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            value.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        value.setSelection(value.getText().length());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawBottomDivider) {
            int y = getHeight() - 1;
            canvas.drawLine(dividerIndent, y, getRight(), y, paint);
        }
        if (drawTopDivider) {
            canvas.drawLine(0, 0, getRight(), 0, paint);
        }
    }
}
