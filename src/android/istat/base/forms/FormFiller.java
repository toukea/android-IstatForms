package android.istat.base.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.istat.base.forms.utils.ViewUtil;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author istat
 */
public final class FormFiller extends FormGetSetter {

	FormFiller(Form form) {
		super(form);
	}

	public static FormFiller fillFromView(Form form, View view,
			boolean editableOnly, FieldValueGetter<?, ?>... handlers) {
		return fillFromView(form, view, editableOnly, handlers);
	}

	public static FormFiller fillFromView(Form form, View view,
			FieldValueGetter<?, ?>... fieldHandlers) {
		return fillFromView(form, view, false,
				fieldHandlers != null ? Arrays.asList(fieldHandlers) : null);
	}

	public static FormFiller fillFromView(Form form, View view,
			boolean editableOnly, List<FieldValueGetter<?, ?>> fieldHandlers) {
		FormFiller filler = new FormFiller(form);
		List<FieldValueGetSetter> handlers = new ArrayList<FieldValueGetSetter>();
		if (fieldHandlers != null && fieldHandlers.size() > 0) {
			handlers.addAll(fieldHandlers);
		}
		filler.addAllFieldHandlers(handlers);
		filler.setModifyEditableOnly(editableOnly);
		filler.mutateView(view);
		return filler;
	}

	public static abstract class FieldValueGetter<T, V extends View> extends
			FieldValueGetSetter {
		public abstract T getValue(V v);

		@SuppressWarnings("unchecked")
		@Override
		public final boolean onHandle(Form form, String fieldName, View view) {
			if (isHandlable(view)) {
				T value = getValue((V) view);
				Log.d("onHandle", "view_Value=" + value);
				if (value != null) {
					form.put(fieldName, value);
					return true;
				}
			}
			return super.onHandle(form, fieldName, view);
		}
	}

	public final static FieldValueGetter<Integer, Spinner> GETTER_SPINNER_INDEX = new FieldValueGetter<Integer, Spinner>() {
		@Override
		public Integer getValue(Spinner spinner) {
			return null;
		}
	};
	public final static FieldValueGetter<String, Spinner> GETTER_SPINNER_CONTAINT = new FieldValueGetter<String, Spinner>() {
		@Override
		public String getValue(Spinner spinner) {
			return null;
		}
	};

	final static FieldValueGetter<Object, View> DEFAULT_GETTER = new FieldValueGetter<Object, View>() {

		@Override
		public Object getValue(View v) {
			Log.d("onHandle", "view_Type=" + v);
			try {
				if (v instanceof TextView) {
					TextView t = (TextView) v;
					return t.getText().toString();
				} else if (v instanceof EditText) {
					EditText t = (EditText) v;
					return t.getText().toString();
				} else if (v instanceof CheckBox) {
					CheckBox t = (CheckBox) v;
					return t.isChecked();
				} else if (v instanceof Spinner) {
					Spinner t = (Spinner) v;
					return t.getSelectedItemPosition();
				} else if (v instanceof RadioButton) {
					RadioButton t = (RadioButton) v;
					return t.isChecked();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	};

	@Override
	protected final FieldValueGetSetter getDefaultHandler() {
		return DEFAULT_GETTER;
	}

	@Override
	protected void traitViewAsViewGroup(ViewGroup v) {
		List<View> childV = !isModifyEditableOnly() ? ViewUtil
				.getDirectChildViews(v) : v.getTouchables();
		for (View view : childV) {
			if (view != null) {
				if (view instanceof ViewGroup) {
					mutateView((ViewGroup) view);
				} else {
					traitViewAsSimpleView(view);
				}
			}
		}
	}
}
