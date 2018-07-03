package nl.weijzen.maastricht.medewerkervandemaand;

import android.content.Context;
import android.content.res.Resources;

import java.util.Calendar;

public class CurrentYearAndMonth {
    private Context context;
    private Integer year;
    private Integer month;

    // Constructors
    public CurrentYearAndMonth(Context context){
        this.context = context;
        Calendar currentDate = Calendar.getInstance();
        this.year  = currentDate.get(Calendar.YEAR);
        this.month = currentDate.get(Calendar.MONTH);
    }

    // Getters
    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month + 1;
    }

    public String getMonthText() {
        Resources resources = this.context.getResources();
        String[] months     = resources.getStringArray(R.array.month_abbreviation);
        String monthText    = months[this.month];
        return monthText;
    }
}
