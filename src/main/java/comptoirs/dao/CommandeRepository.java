package comptoirs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import comptoirs.entity.Commande;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    @Query("""
    SELECT SUM(l.quantite * p.prixUnitaire * (1 - c.remise))
    FROM Ligne l
    JOIN l.produit p
    JOIN l.commande c
    WHERE c.numero = :numeroCommande
""")
    BigDecimal montantArticles(@Param("numeroCommande") Integer numeroCommande);

    @Query("""
    SELECT c.numero, c.saisiele, c.envoyeele, c.port, c.destinataire,
           SUM(l.quantite * p.prixUnitaire * (1 - c.remise))
    FROM Commande c
    JOIN c.lignes l
    JOIN l.produit p
    WHERE c.client.code = :codeClient
    GROUP BY c.numero, c.saisiele, c.envoyeele, c.port, c.destinataire
""")
    List<Object[]> findCommandesByClient(@Param("codeClient") String codeClient);


}
