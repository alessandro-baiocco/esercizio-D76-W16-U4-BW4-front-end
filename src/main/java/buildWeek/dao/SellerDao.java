package buildWeek.dao;

import buildWeek.entities.Seller;
import buildWeek.entities.Ticket;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class SellerDao {
    private final EntityManager em;

    public SellerDao(EntityManager em) {
        this.em = em;
    }

    public void save(Seller s) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        em.persist(s);

        transaction.commit();
        System.out.println("Nuovo seller salvato correttamente");
    }

    public Seller getById(int id) {
        return em.find(Seller.class, id);
    }


    public void getByIsbnAndDelete(int id) {

        Seller found = em.find(Seller.class, id);

        if (found != null) {
            EntityTransaction transaction = em.getTransaction();

            transaction.begin();
            em.remove(found);
            transaction.commit();
            System.out.println("Il seller è stato cancellato correttamente");
        } else {
            System.err.println("Il seller con l'id " + id + " non è stato trovato");
        }
    }


    public void getByIdAndDelete(int id) {

        Seller found = em.find(Seller.class, id);

        if (found != null) {
            EntityTransaction transaction = em.getTransaction();

            transaction.begin();
            em.remove(found);
            transaction.commit();
            System.out.println("Il seller è stato cancellato correttamente");
        } else {
            System.err.println("Il seller con l'id " + id + " non è stato trovato");
        }

    }

    public Seller getRandomSeller() {
        TypedQuery<Seller> query = em.createQuery("SELECT s FROM Seller s ORDER BY RAND()", Seller.class);
        query.setMaxResults(1);
        return query.getSingleResult();
    }

    public boolean isActive(Seller seller) {
        return seller.isLicensed();
    }

    public int soldTickets(Seller seller) {
        TypedQuery<Ticket> query = em.createQuery("SELECT t FROM Ticket t WHERE t.seller = :seller", Ticket.class);
        query.setParameter("seller", seller);
        return query.getResultList().size();
    }

    public int soldTicketsDay(Seller seller, LocalDate date) {
        TypedQuery<Ticket> query = em.createQuery("SELECT t FROM Ticket t WHERE t.seller = :seller AND t.createdDate = :date", Ticket.class);
        query.setParameter("seller", seller);
        query.setParameter("date", date);
        return query.getResultList().size();
    }

    public int soldTicketsPeriod(Seller seller, LocalDate date1, LocalDate date2) {
        TypedQuery<Ticket> query = em.createQuery("SELECT t FROM Ticket t WHERE t.seller = :seller AND t.createdDate BETWEEN :date1 AND :date2", Ticket.class);
        query.setParameter("seller", seller);
        query.setParameter("date1", date1);
        query.setParameter("date2", date2);
        return query.getResultList().size();
    }

    public int soldSubscriptions(Seller seller) {
        TypedQuery<Ticket> query = em.createQuery("SELECT s FROM Subscription s WHERE s.seller = :seller", Ticket.class);
        query.setParameter("seller", seller);
        return query.getResultList().size();
    }

    public int soldSubscriptionsDay(Seller seller, LocalDate date) {
        TypedQuery<Ticket> query = em.createQuery("SELECT s FROM Subscription s WHERE s.seller = :seller AND s.createdDate = :date", Ticket.class);
        query.setParameter("seller", seller);
        query.setParameter("date", date);
        return query.getResultList().size();
    }

    public int soldSubscriptionsPeriod(Seller seller, LocalDate date1, LocalDate date2) {
        TypedQuery<Ticket> query = em.createQuery("SELECT s FROM Subscription s WHERE s.seller = :seller AND s.createdDate BETWEEN :date1 AND :date2", Ticket.class);
        query.setParameter("seller", seller);
        query.setParameter("date1", date1);
        query.setParameter("date2", date2);
        return query.getResultList().size();
    }

    public Seller getRandomSell() {
        TypedQuery<Seller> query = em.createQuery("SELECT s FROM Seller s ORDER BY RAND()", Seller.class);
        query.setMaxResults(1);
        return query.getSingleResult();
    }

    public List<Seller> distributorsActive() {
        TypedQuery<Seller> query = em.createQuery("SELECT s FROM Seller s WHERE s.licensed is true", Seller.class);
        return query.getResultList();
    }


}
