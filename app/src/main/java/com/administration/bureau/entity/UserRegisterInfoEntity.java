package com.administration.bureau.entity;

/**
 * Created by omyrobin on 2017/4/17.
 */

public class UserRegisterInfoEntity {

    /**
     * avatar :
     * passport_image : http://www.myfangshan.com/uploads/58e617cf73332.jpg
     * country : 1
     * credential_type : 1
     * credential : 1212
     * credential_expired_date : 2017-02-12
     * person_type : 1
     * person_area_type : 1
     * firstname : SAN
     * lastname : ZHANG
     * chinese_name : 张三
     * gender : 0
     * birthday : 1988-05-20
     * occupation : 1
     * working_organization : CCTV
     * phone : 15789890798
     * emergency_contact : TESTER
     * emergency_phone : 15767678765
     * enter_image : http://www.myfangshan.com/uploads/58e617cf73332.jpg
     * visa_image : http://www.myfangshan.com/uploads/58e617cf73332.jpg
     * visa_type : 1
     * visa_expired_date : 2017-09-08
     * entry_date : 2017-09-08
     * entry_port : 1
     * stay_reason : 1
     * stay_expired_date : 2017-09-08
     * checkin_date : 2017-09-08
     * checkout_date : 2017-09-08
     * house_address : GUANGZHOU
     * police_station : 1
     * community : 1
     * house_type : 1
     * landlord_country : 1
     * landlord_identity : 69856958948596
     * landlord_name : 李四
     * landlord_gender : 0
     * landlord_phone : 13768689097
     * certificate_image :
     * user_id : 51
     * reject_reason: "没写对",
     * updated_at : 2017-04-06 19:18:52
     * created_at : 2017-04-06 19:18:52
     * id : 31
     */

    private String avatar;
    private String passport_image;
    private String country;
    private String credential_type;
    private String credential;
    private String credential_expired_date;
    private String person_type;
    private String person_area_type;
    private String firstname;
    private String lastname;
    private String chinese_name;
    private String gender;
    private String birthday;
    private String birthplace;
    private String occupation;
    private String working_organization;
    private String phone;
    private String emergency_contact;
    private String emergency_phone;
    private String enter_image;
    private String visa_image;
    private String visa_type;
    private String visa_expired_date;
    private String entry_date;
    private String entry_port;
    private String stay_reason;
    private String stay_expired_date;
    private String checkin_date;
    private String checkout_date;
    private String house_address;
    private String police_station;
    private String community;
    private String house_type;
    private String landlord_country;
    private String landlord_identity;
    private String landlord_name;
    private String landlord_gender;
    private String landlord_phone;
    private String certificate_image;
    private String reject_reason;
    private String user_id;
    private String updated_at;
    private String created_at;
    private int id;
    private int status;// 0=未审核 1=未通过 2=通过 3=电子证 =4已核销

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassport_image() {
        return passport_image;
    }

    public void setPassport_image(String passport_image) {
        this.passport_image = passport_image;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCredential_type() {
        return credential_type;
    }

    public void setCredential_type(String credential_type) {
        this.credential_type = credential_type;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getCredential_expired_date() {
        return credential_expired_date;
    }

    public void setCredential_expired_date(String credential_expired_date) {
        this.credential_expired_date = credential_expired_date;
    }

    public String getPerson_type() {
        return person_type;
    }

    public void setPerson_type(String person_type) {
        this.person_type = person_type;
    }

    public String getPerson_area_type() {
        return person_area_type;
    }

    public void setPerson_area_type(String person_area_type) {
        this.person_area_type = person_area_type;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getChinese_name() {
        return chinese_name;
    }

    public void setChinese_name(String chinese_name) {
        this.chinese_name = chinese_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getWorking_organization() {
        return working_organization;
    }

    public void setWorking_organization(String working_organization) {
        this.working_organization = working_organization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(String emergency_contact) {
        this.emergency_contact = emergency_contact;
    }

    public String getEmergency_phone() {
        return emergency_phone;
    }

    public void setEmergency_phone(String emergency_phone) {
        this.emergency_phone = emergency_phone;
    }

    public String getEnter_image() {
        return enter_image;
    }

    public void setEnter_image(String enter_image) {
        this.enter_image = enter_image;
    }

    public String getVisa_image() {
        return visa_image;
    }

    public void setVisa_image(String visa_image) {
        this.visa_image = visa_image;
    }

    public String getVisa_type() {
        return visa_type;
    }

    public void setVisa_type(String visa_type) {
        this.visa_type = visa_type;
    }

    public String getVisa_expired_date() {
        return visa_expired_date;
    }

    public void setVisa_expired_date(String visa_expired_date) {
        this.visa_expired_date = visa_expired_date;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getEntry_port() {
        return entry_port;
    }

    public void setEntry_port(String entry_port) {
        this.entry_port = entry_port;
    }

    public String getStay_reason() {
        return stay_reason;
    }

    public void setStay_reason(String stay_reason) {
        this.stay_reason = stay_reason;
    }

    public String getStay_expired_date() {
        return stay_expired_date;
    }

    public void setStay_expired_date(String stay_expired_date) {
        this.stay_expired_date = stay_expired_date;
    }

    public String getCheckin_date() {
        return checkin_date;
    }

    public void setCheckin_date(String checkin_date) {
        this.checkin_date = checkin_date;
    }

    public String getCheckout_date() {
        return checkout_date;
    }

    public void setCheckout_date(String checkout_date) {
        this.checkout_date = checkout_date;
    }

    public String getHouse_address() {
        return house_address;
    }

    public void setHouse_address(String house_address) {
        this.house_address = house_address;
    }

    public String getPolice_station() {
        return police_station;
    }

    public void setPolice_station(String police_station) {
        this.police_station = police_station;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getLandlord_country() {
        return landlord_country;
    }

    public void setLandlord_country(String landlord_country) {
        this.landlord_country = landlord_country;
    }

    public String getLandlord_identity() {
        return landlord_identity;
    }

    public void setLandlord_identity(String landlord_identity) {
        this.landlord_identity = landlord_identity;
    }

    public String getLandlord_name() {
        return landlord_name;
    }

    public void setLandlord_name(String landlord_name) {
        this.landlord_name = landlord_name;
    }

    public String getLandlord_gender() {
        return landlord_gender;
    }

    public void setLandlord_gender(String landlord_gender) {
        this.landlord_gender = landlord_gender;
    }

    public String getLandlord_phone() {
        return landlord_phone;
    }

    public void setLandlord_phone(String landlord_phone) {
        this.landlord_phone = landlord_phone;
    }

    public String getCertificate_image() {
        return certificate_image;
    }

    public void setCertificate_image(String certificate_image) {
        this.certificate_image = certificate_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserRegisterInfoEntity{" +
                "passport_image='" + passport_image + '\'' +
                ", country='" + country + '\'' +
                ", credential_type='" + credential_type + '\'' +
                ", credential='" + credential + '\'' +
                ", credential_expired_date='" + credential_expired_date + '\'' +
                ", person_type='" + person_type + '\'' +
                ", person_area_type='" + person_area_type + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", chinese_name='" + chinese_name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", occupation='" + occupation + '\'' +
                ", working_organization='" + working_organization + '\'' +
                ", phone='" + phone + '\'' +
                ", emergency_contact='" + emergency_contact + '\'' +
                ", emergency_phone='" + emergency_phone + '\'' +
                ", enter_image='" + enter_image + '\'' +
                ", visa_image='" + visa_image + '\'' +
                ", visa_type='" + visa_type + '\'' +
                ", visa_expired_date='" + visa_expired_date + '\'' +
                ", entry_date='" + entry_date + '\'' +
                ", entry_port='" + entry_port + '\'' +
                ", stay_reason='" + stay_reason + '\'' +
                ", stay_expired_date='" + stay_expired_date + '\'' +
                ", checkin_date='" + checkin_date + '\'' +
                ", checkout_date='" + checkout_date + '\'' +
                ", house_address='" + house_address + '\'' +
                ", police_station='" + police_station + '\'' +
                ", community='" + community + '\'' +
                ", house_type='" + house_type + '\'' +
                ", landlord_country='" + landlord_country + '\'' +
                ", landlord_identity='" + landlord_identity + '\'' +
                ", landlord_name='" + landlord_name + '\'' +
                ", landlord_gender='" + landlord_gender + '\'' +
                ", landlord_phone='" + landlord_phone + '\'' +
                ", certificate_image='" + certificate_image + '\'' +
                ", user_id=" + user_id +
                ", updated_at='" + updated_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", id=" + id +
                '}';
    }
}
