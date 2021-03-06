/*
 * Copyright (C) 2013 - 2018, Logical Clocks AB and RISE SICS AB. All rights reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS  OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package io.hops.hopsworks.common.dao.metadata.db;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import io.hops.hopsworks.common.dao.AbstractFacade;
import io.hops.hopsworks.common.dao.metadata.RawData;
import io.hops.hopsworks.common.dao.metadata.RawDataPK;
import io.hops.hopsworks.common.metadata.exception.DatabaseException;

@Stateless
public class RawDataFacade extends AbstractFacade<RawData> {

  private static final Logger logger = Logger.getLogger(RawDataFacade.class.
          getName());

  @PersistenceContext(unitName = "kthfsPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public RawDataFacade() {
    super(RawData.class);
  }

  public RawData getRawData(RawDataPK rawdataPK) throws DatabaseException {

    TypedQuery<RawData> q = this.em.createNamedQuery("RawData.findByPrimaryKey",
            RawData.class);
    q.setParameter("rawdataPK", rawdataPK);

    try {
      return q.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * adds a new record into 'raw_data' table. RawData is the object that's
   * going to be persisted/updated in the database
   * <p/>
   * @param raw
   * @throws se.kth.hopsworks.meta.exception.DatabaseException
   */
  public void addRawData(RawData raw) throws DatabaseException {

    try {
      RawData r = this.contains(raw) ? raw : this.getRawData(raw.getRawdataPK());

      if (r != null && r.getRawdataPK().getTupleid() != -1 && r.getRawdataPK().
              getFieldid() != -1) {
        /*
         * if the row exists just update it.
         */
        r.copy(raw);
        this.em.merge(r);
      } else {
        /*
         * if the row is new then just persist it
         */
        r = raw;
        this.em.persist(r);
      }

      this.em.flush();
      this.em.clear();
    } catch (IllegalStateException | SecurityException e) {

      throw new DatabaseException("Could not add raw data " + raw, e);
    }
  }

  public int getLastInsertedTupleId() throws DatabaseException {

    String queryString = "RawData.lastInsertedTupleId";

    Query query = this.em.createNamedQuery(queryString);
    List<RawData> list = query.getResultList();

    return (!list.isEmpty()) ? list.get(0).getId() : 0;
  }

  /**
   * Checks if a raw data instance is a managed entity
   * <p/>
   * @param rawdata
   * @return
   */
  public boolean contains(RawData rawdata) {
    return this.em.contains(rawdata);
  }
}
