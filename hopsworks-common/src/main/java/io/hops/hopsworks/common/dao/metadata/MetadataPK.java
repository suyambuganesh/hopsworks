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

package io.hops.hopsworks.common.dao.metadata;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

@Embeddable
public class MetadataPK implements Serializable {

  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Basic(optional = false)
  @NotNull
  @Column(name = "id")
  private Integer id;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fieldid")
  private int fieldid;

  @Basic(optional = false)
  @NotNull
  @Column(name = "tupleid")
  private int tupleid;

  public MetadataPK() {
  }

  public MetadataPK(int id, int fieldid, int tupleid) {
    this.id = id;
    this.fieldid = fieldid;
    this.tupleid = tupleid;
  }

  public void copy(MetadataPK metadataPK) {
    this.id = metadataPK.getId();
    this.fieldid = metadataPK.getFieldid();
    this.tupleid = metadataPK.getTupleid();
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setFieldid(int fieldid) {
    this.fieldid = fieldid;
  }

  public void setTupleid(int tupleid) {
    this.tupleid = tupleid;
  }

  public int getId() {
    return this.id;
  }

  public int getFieldid() {
    return this.fieldid;
  }

  public int getTupleid() {
    return this.tupleid;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (int) this.id;
    hash += (int) this.fieldid;
    hash += (int) this.tupleid;
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof MetadataPK)) {
      return false;
    }
    MetadataPK other = (MetadataPK) object;
    if (!this.id.equals(other.id)) {
      return false;
    }
    if (this.fieldid != other.fieldid) {
      return false;
    }

    return this.tupleid == other.tupleid;
  }

  @Override
  public String toString() {
    return "se.kth.meta.entity.MetaDataPK[ id=" + this.id + ", fieldid="
            + this.fieldid + ", tupleid= " + this.tupleid + " ]";
  }
}
