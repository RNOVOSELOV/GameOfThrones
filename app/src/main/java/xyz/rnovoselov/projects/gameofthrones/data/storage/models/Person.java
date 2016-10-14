package xyz.rnovoselov.projects.gameofthrones.data.storage.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import java.util.List;

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

    @Generated(hash = 1047763414)
    public Person(Long id, @NotNull Long personRemoteId, @NotNull Long personHouseRemoteId,
            @NotNull String name, Boolean sex, String born, Long father, Long mother) {
        this.id = id;
        this.personRemoteId = personRemoteId;
        this.personHouseRemoteId = personHouseRemoteId;
        this.name = name;
        this.sex = sex;
        this.born = born;
        this.father = father;
        this.mother = mother;
    }

    @Generated(hash = 1024547259)
    public Person() {
    }

    @Id
    private Long id;

    @NotNull
    @Unique
    private Long personRemoteId;

    @NotNull
    private Long personHouseRemoteId;

    @NotNull
    private String name;

    private Boolean sex;

    private String born;

    private Long father;

    private Long mother;

    @ToMany(joinProperties = {
            @JoinProperty(name = "personRemoteId", referencedName = "titlePersonRemoteId")
    })
    private List<Titles> characteristics;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1362716280)
    public List<Titles> getCharacteristics() {
        if (characteristics == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TitlesDao targetDao = daoSession.getTitlesDao();
            List<Titles> characteristicsNew = targetDao
                    ._queryPerson_Characteristics(personRemoteId);
            synchronized (this) {
                if (characteristics == null) {
                    characteristics = characteristicsNew;
                }
            }
        }
        return characteristics;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 146028633)
    public synchronized void resetCharacteristics() {
        characteristics = null;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2056799268)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPersonDao() : null;
    }
}
