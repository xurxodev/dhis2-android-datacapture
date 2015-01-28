package org.hisp.dhis.mobile.datacapture.ui.adapters.rows;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import org.hisp.dhis.mobile.datacapture.R;
import org.hisp.dhis.mobile.datacapture.api.models.Field;

public class RadioButtonsRow implements Row {
    private static final String EMPTY_FIELD = "";
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    public static final String FEMALE = "gender_female";
    public static final String MALE = "gender_male";
    public static final String OTHER = "gender_other";

    private final Field mField;
    private final RowTypes mType;
    
    public RadioButtonsRow(Field field, RowTypes type) {
        if (!RowTypes.GENDER.equals(type) && !RowTypes.BOOLEAN.equals(type)) {
            throw new IllegalArgumentException("Unsupported row type");
        }

        mField = field;
        mType = type;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView, ViewGroup container) {
        View view;
        BooleanRowHolder holder;
        
        if (convertView == null) {
            View root = inflater.inflate(
                    R.layout.listview_row_radio_buttons, container, false);
            TextView label = (TextView)
                    root.findViewById(R.id.text_label);
            CompoundButton firstButton = (CompoundButton)
                    root.findViewById(R.id.first_radio_button);
            CompoundButton secondButton = (CompoundButton)
                    root.findViewById(R.id.second_radio_button);
            CompoundButton thirdButton = (CompoundButton)
                    root.findViewById(R.id.third_radio_button);

            if (RowTypes.BOOLEAN.equals(mType)) {
                firstButton.setText(R.string.yes);
                secondButton.setText(R.string.no);
                thirdButton.setText(R.string.none);
            } else if (RowTypes.GENDER.equals(mType)) {
                firstButton.setText(R.string.gender_male);
                secondButton.setText(R.string.gender_female);
                thirdButton.setText(R.string.gender_other);
            }

            CheckedChangeListener listener = new CheckedChangeListener();
            holder = new BooleanRowHolder(mType, label, firstButton,
                    secondButton, thirdButton, listener);

            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (BooleanRowHolder) convertView.getTag();
        }

        holder.updateViews(mField);
        return view;
    }

    @Override
    public int getViewType() {
        return mType.ordinal();
    }
    
    private static class BooleanRowHolder {
        final TextView textLabel;
        final CompoundButton firstButton;
        final CompoundButton secondButton;
        final CompoundButton thirdButton;
        final CheckedChangeListener listener;
        final RowTypes type;
        
        public BooleanRowHolder(RowTypes type, TextView textLabel, CompoundButton firstButton,
                         CompoundButton secondButton, CompoundButton thirdButton,
                         CheckedChangeListener listener) {
            this.type = type;
            this.textLabel = textLabel;
            this.firstButton = firstButton;
            this.secondButton = secondButton;
            this.thirdButton = thirdButton;
            this.listener = listener;
        }

        public void updateViews(Field field) {
            textLabel.setText(field.getLabel());

            listener.setType(type);
            listener.setField(field);

            firstButton.setOnCheckedChangeListener(listener);
            secondButton.setOnCheckedChangeListener(listener);
            thirdButton.setOnCheckedChangeListener(listener);

            if (RowTypes.BOOLEAN.equals(type)) {
                if (TRUE.equalsIgnoreCase(field.getValue())) {
                    firstButton.setChecked(true);
                } else if (FALSE.equalsIgnoreCase(field.getValue())) {
                    secondButton.setChecked(true);
                } else if (EMPTY_FIELD.equalsIgnoreCase(field.getValue())) {
                    thirdButton.setChecked(true);
                }
            } else if (RowTypes.GENDER.equals(type)) {
                if (MALE.equals(field.getValue())) {
                    firstButton.setChecked(true);
                } else if (FEMALE.equals(field.getValue())) {
                    secondButton.setChecked(true);
                } else if (OTHER.equals(field.getValue())) {
                    thirdButton.setChecked(true);
                }
            }
        }
    }

    private static class CheckedChangeListener implements OnCheckedChangeListener {
        private Field field;
        private RowTypes type;

        public void setField(Field field) {
            this.field = field;
        }

        public void setType(RowTypes type) {
            this.type = type;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.first_radio_button: {
                    if (isChecked) {
                        if (RowTypes.BOOLEAN.equals(type)) {
                            field.setValue(TRUE);
                        } else if (RowTypes.GENDER.equals(type)) {
                            field.setValue(MALE);
                        }
                    }
                    break;
                }
                case R.id.second_radio_button: {
                    if (isChecked) {
                        if (RowTypes.BOOLEAN.equals(type)) {
                            field.setValue(FALSE);
                        } else if (RowTypes.GENDER.equals(type)) {
                            field.setValue(FEMALE);
                        }
                    }
                    break;
                }
                case R.id.third_radio_button: {
                    if (isChecked) {
                        if (RowTypes.BOOLEAN.equals(type)) {
                            field.setValue(EMPTY_FIELD);
                        } else if (RowTypes.GENDER.equals(type)) {
                            field.setValue(OTHER);
                        }
                    }
                    break;
                }

            }
        }
    }
    
}




