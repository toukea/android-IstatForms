package android.istat.base.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.istat.base.forms.tools.FormTools;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author istat
 */
public final class FormFlower extends FormGetSetter {

    FormFlower(Form form) {
        super(form);
    }

    public static void flowIntoView(Form form, View view,
                                    FieldValueSetter<?, ?>... fieldHandlers) {
        flowIntoView(form, view, false, fieldHandlers);
    }

    public static void flowIntoView(Form form, View view,
                                    boolean editableOnly, FieldValueSetter<?, ?>... fieldHandlers) {
        flowIntoView(form, view, editableOnly,
                fieldHandlers != null ? Arrays.asList(fieldHandlers) : null);
    }

    public static void flowIntoView(Form form, View view,
                                    boolean editableOnly, List<FieldValueSetter<?, ?>> fieldHandlers) {
        FormFlower flower = new FormFlower(form);
        List<FieldValueGetSetter> handlers = new ArrayList<FieldValueGetSetter>();
        if (fieldHandlers != null && fieldHandlers.size() > 0) {
            handlers.addAll(fieldHandlers);
        }
        flower.setEditableOnlyGetSettable(editableOnly);
        flower.handleView(view);

    }

    public static abstract class FieldValueSetter<T, V extends View> extends
            FieldValueGetSetter {

        public abstract void setValue(T entity, V v);

        @SuppressWarnings("unchecked")
        @Override
        public final boolean onHandle(Form form, String fieldName, View view) {
            if (isHandleAble(view)) {
                T value = form.opt(fieldName);
                setValue(value, (V) view);
                return true;
            }
            return false;
        }
    }

    public final static FieldValueSetter<Integer, Spinner> SETTER_SPINNER_INDEX = new FieldValueSetter<Integer, Spinner>() {

        @Override
        public void setValue(Integer entity, Spinner v) {
            v.setSelection(FormTools.parseInt(entity));
        }
    };
    public final static FieldValueSetter<String, Spinner> SETTER_SPINNER_CONTAINT = new FieldValueSetter<String, Spinner>() {

        @Override
        public void setValue(String entity, Spinner spinner) {
        }
    };

    public abstract static class FieldFlower<V extends View> extends FieldValueSetter<Object, V> {

    }

    public final static FieldFlower<TextView> SETTER_TEXT_çVIEW_TEXT = new FieldFlower<TextView>() {

        @Override
        public void setValue(Object value, TextView v) {
            v.setText(FormTools.parseString(value));
        }
    };
    public final static FieldFlower<CheckBox> SETTER_CHECKBOX_STATE = new FieldFlower<CheckBox>() {

        @Override
        public void setValue(Object value, CheckBox v) {

            v.setChecked(FormTools.parseBoolean(value));

        }
    };
    public final static FieldFlower<RadioButton> SETTER_RADIO_BUTTON_STATE = new FieldFlower<RadioButton>() {

        @Override
        public void setValue(Object value, RadioButton v) {
            v.setChecked(FormTools.parseBoolean(value));
        }
    };
    public final static FieldFlower<RadioGroup> SETTER_RADIO_GROUP_SELECTION_TEXT = new FieldFlower<RadioGroup>() {

        @Override
        public void setValue(Object value, RadioGroup v) {

        }
    };

    @Override
    protected final List<FieldValueGetSetter> getDefaultHandlers() {
        return new ArrayList() {
            {
                add(SETTER_SPINNER_INDEX);
                add(SETTER_TEXT_çVIEW_TEXT);
                add(SETTER_CHECKBOX_STATE);
                add(SETTER_RADIO_BUTTON_STATE);
                add(SETTER_RADIO_GROUP_SELECTION_TEXT);
            }
        };
    }

    protected final void handleViewGroup(ViewGroup v) {
        String[] fields = form.getFieldNames();
        for (String field : fields) {
            View view = v.findViewWithTag(field);
            applyGetSetter(view);
        }
    }
}
