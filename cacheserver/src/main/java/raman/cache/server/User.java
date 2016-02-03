package raman.cache.server;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*****************************************************************************************************************************
 * Developer : Ramanpreet Singh Khinda
 * <p/>
 * This class represents fields in the User table
 *****************************************************************************************************************************/
@DatabaseTable(tableName = "users")
public class User {
    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private int userId;

    @DatabaseField
    private String device_imei;

    @DatabaseField
    private String gcm_reg_id;

    public User() {
    }

    public User(String device_imei, String gcm_reg_id) {
        this.device_imei = device_imei;
        this.gcm_reg_id = gcm_reg_id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDeviceImei() {
        return device_imei;
    }

    public void setDeviceImei(String device_imei) {
        this.device_imei = device_imei;
    }

    public String getGcmRegId() {
        return gcm_reg_id;
    }

    public void setGcmRegId(String gcm_reg_id) {
        this.gcm_reg_id = gcm_reg_id;
    }

    @Override
    public String toString() {
        return userId + " : " + device_imei + " : " + gcm_reg_id;
    }
}
