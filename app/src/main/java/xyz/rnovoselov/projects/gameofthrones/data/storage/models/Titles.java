package xyz.rnovoselov.projects.gameofthrones.data.storage.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by roman on 13.10.16.
 */

@Entity(active = true, nameInDb = "TITLES")
public class Titles {

    public Titles(Long remoteHomeId, String houseTitle) {
        this.houseRemoteId = remoteHomeId;
        this.houseTitle = houseTitle;
    }

    @Id
    private Long id;

    @NotNull
    private Long houseRemoteId;

    @NotNull
    @Unique
    private String houseTitle;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 658445912)
    private transient TitlesDao myDao;

    @Generated(hash = 434833637)
    public Titles(Long id, @NotNull Long houseRemoteId,
                  @NotNull String houseTitle) {
        this.id = id;
        this.houseRemoteId = houseRemoteId;
        this.houseTitle = houseTitle;
    }

    @Generated(hash = 2082159104)
    public Titles() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHouseRemoteId() {
        return this.houseRemoteId;
    }

    public void setHouseRemoteId(Long houseRemoteId) {
        this.houseRemoteId = houseRemoteId;
    }

    public String getHouseTitle() {
        return this.houseTitle;
    }

    public void setHouseTitle(String houseTitle) {
        this.houseTitle = houseTitle;
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
    @Generated(hash = 1913718169)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTitlesDao() : null;
    }
}
