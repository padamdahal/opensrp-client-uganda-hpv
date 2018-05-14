package org.smartregister.ug.hpv.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.common.collect.ImmutableMap;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.ug.hpv.application.HpvApplication;
import org.smartregister.ug.hpv.util.DBConstants;
import org.smartregister.ug.hpv.util.Utils;

import java.util.Map;

/**
 * Created by ndegwamartin on 25/04/2018.
 */

public class PatientRepository {

    private static final String TAG = PatientRepository.class.getCanonicalName();

    public static Map<String, String> getPatientContacts(String baseEntityId) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = HpvApplication.getInstance().getRepository().getReadableDatabase();

            String query = "SELECT " + DBConstants.KEY.CARETAKER_NAME + "," + DBConstants.KEY.CARETAKER_PHONE + "," + DBConstants.KEY.VHT_NAME + "," + DBConstants.KEY.VHT_PHONE + " FROM " + DBConstants.PATIENT_TABLE_NAME + " WHERE " + DBConstants.KEY.BASE_ENTITY_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{baseEntityId});
            if (cursor != null && cursor.moveToFirst()) {

                if (cursor.getString(cursor.getColumnIndex(DBConstants.KEY.VHT_PHONE)) != null) {
                    return ImmutableMap.of(DBConstants.KEY.CARETAKER_NAME, cursor.getString(cursor.getColumnIndex(DBConstants.KEY.CARETAKER_NAME)), DBConstants.KEY.CARETAKER_PHONE, cursor.getString(cursor.getColumnIndex(DBConstants.KEY.CARETAKER_PHONE)), DBConstants.KEY.VHT_NAME, cursor.getString(cursor.getColumnIndex(DBConstants.KEY.VHT_NAME)), DBConstants.KEY.VHT_PHONE, cursor.getString(cursor.getColumnIndex(DBConstants.KEY.VHT_PHONE)));
                } else {
                    return ImmutableMap.of(DBConstants.KEY.CARETAKER_NAME, cursor.getString(cursor.getColumnIndex(DBConstants.KEY.CARETAKER_NAME)), DBConstants.KEY.CARETAKER_PHONE, cursor.getString(cursor.getColumnIndex(DBConstants.KEY.CARETAKER_PHONE)));

                }

            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static void updateDoseDates(String baseEntityID, String date, String doseNumber) {

        try {
            SQLiteDatabase db = HpvApplication.getInstance().getRepository().getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("date_dose_" + doseNumber + "_given", date);
            values.put("dose_two_date", Utils.calculateVaccineDueDate(date));
            db.update(DBConstants.PATIENT_TABLE_NAME, values, DBConstants.KEY.BASE_ENTITY_ID + " = ?", new String[]{baseEntityID});
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
}
