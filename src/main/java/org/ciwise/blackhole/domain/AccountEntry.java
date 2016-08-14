package org.ciwise.blackhole.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A AccountEntry.
 */
@Entity
@Table(name = "account_entry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "accountentry")
public class AccountEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "entrydate")
    private LocalDate entrydate;

    @Column(name = "transaction")
    private String transaction;

    @Column(name = "reconcile")
    private Boolean reconcile;

    @Column(name = "postingref")
    private Long postingref;

    @Column(name = "debit")
    private String debit;

    @Column(name = "credit")
    private String credit;

    @Column(name = "debitbalance")
    private String debitbalance;

    @Column(name = "creditbalance")
    private String creditbalance;

    @Column(name = "notes")
    private String notes;

    @Column(name = "cno")
    private String cno;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(LocalDate entrydate) {
        this.entrydate = entrydate;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public Boolean isReconcile() {
        return reconcile;
    }

    public void setReconcile(Boolean reconcile) {
        this.reconcile = reconcile;
    }

    public Long getPostingref() {
        return postingref;
    }

    public void setPostingref(Long postingref) {
        this.postingref = postingref;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebitbalance() {
        return debitbalance;
    }

    public void setDebitbalance(String debitbalance) {
        this.debitbalance = debitbalance;
    }

    public String getCreditbalance() {
        return creditbalance;
    }

    public void setCreditbalance(String creditbalance) {
        this.creditbalance = creditbalance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountEntry accountEntry = (AccountEntry) o;
        if(accountEntry.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, accountEntry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountEntry{" +
            "id=" + id +
            ", entrydate='" + entrydate + "'" +
            ", transaction='" + transaction + "'" +
            ", reconcile='" + reconcile + "'" +
            ", postingref='" + postingref + "'" +
            ", debit='" + debit + "'" +
            ", credit='" + credit + "'" +
            ", debitbalance='" + debitbalance + "'" +
            ", creditbalance='" + creditbalance + "'" +
            ", notes='" + notes + "'" +
            ", cno='" + cno + "'" +
            '}';
    }
}