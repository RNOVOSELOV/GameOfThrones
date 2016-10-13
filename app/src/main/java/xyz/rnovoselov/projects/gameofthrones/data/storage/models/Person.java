package xyz.rnovoselov.projects.gameofthrones.data.storage.models;

import android.util.Log;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import xyz.rnovoselov.projects.gameofthrones.data.network.res.PersonModelRes;

/**
 * Created by roman on 13.10.16.
 */

@Entity(active = true, nameInDb = "PERSON")
public class Person {

    public Person(Long houseRemoteId, Long personRemoteId, PersonModelRes model) {
        this.personHouseRemoteId = houseRemoteId;
        this.personRemoteId = personRemoteId;
        this.name = model.getName();
        this.sex = null;
        if (model.getGender().equals("Female")) {
            sex = false;
        } else if (model.getGender().equals("Male")) {
            sex = true;
        }
        this.born = model.getBorn();

        this.father = model.getFather();
        this.mother = model.getMother();
    }

    @Generated(hash = 471968301)
    public Person(Long id, @NotNull Long personHouseRemoteId, @NotNull Long personRemoteId,
                  @NotNull String name, Boolean sex, String born, String titles, String aliases,
                  Long father, Long mother) {
        this.id = id;
        this.personHouseRemoteId = personHouseRemoteId;
        this.personRemoteId = personRemoteId;
        this.name = name;
        this.sex = sex;
        this.born = born;
        this.titles = titles;
        this.aliases = aliases;
        this.father = father;
        this.mother = mother;
    }

    @Generated(hash = 1024547259)
    public Person() {
    }

    @Id
    private Long id;

    @NotNull
    private Long personHouseRemoteId;

    @NotNull
    @Unique
    private Long personRemoteId;

    @NotNull
    private String name;

    private Boolean sex;

    private String born;

    private String titles;

    private String aliases;

    private Long father;

    private Long mother;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 778611619)
    private transient PersonDao myDao;

    public Long getId() {
        return this.id;
    }

    public Long getPersonHouseRemoteId() {
        return this.personHouseRemoteId;
    }

    public Boolean getSex() {
        return this.sex;
    }

    public String getBorn() {
        return this.born;
    }

    public String getTitles() {
        return this.titles;
    }

    public String getAliases() {
        return this.aliases;
    }

    public Long getFather() {
        return this.father;
    }

    public Long getMother() {
        return this.mother;
    }

    public String getName() {
        return this.name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPersonHouseRemoteId(Long personHouseRemoteId) {
        this.personHouseRemoteId = personHouseRemoteId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2056799268)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPersonDao() : null;
    }

    public Long getPersonRemoteId() {
        return this.personRemoteId;
    }

    public void setPersonRemoteId(Long personRemoteId) {
        this.personRemoteId = personRemoteId;
    }

    public void setFather(Long father) {
        this.father = father;
    }

    public void setMother(Long mother) {
        this.mother = mother;
    }
}
