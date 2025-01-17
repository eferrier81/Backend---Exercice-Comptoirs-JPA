package comptoirs.dao;

import comptoirs.entity.Commande;
import comptoirs.entity.Ligne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
public class CommandeRepositoryTest {

    @Autowired
    private CommandeRepository commandeDao;

    @Autowired
    private ClientRepository clientDao;

    @Autowired
    private ProduitRepository produitDao;

    private Commande commandeAvecProduits;

    private Commande commandeSansProduits;

    @BeforeEach
    void setUp() {
        var client = clientDao.findById("ALFKI").orElseThrow();
        var chai = produitDao.findById(1).orElseThrow();
        var chang = produitDao.findById(2).orElseThrow();
        // Une commande sans produits
        commandeSansProduits = new Commande();
        commandeSansProduits.setClient(client);
        commandeSansProduits.setPort(new BigDecimal("10.00"));
        commandeSansProduits.setSaisiele(LocalDate.now());
        commandeSansProduits.setRemise(new BigDecimal("0.10")); // 10% remise
        commandeDao.save(commandeSansProduits);
        // Une commande avec des produits
        commandeAvecProduits = new Commande();
        commandeAvecProduits.setClient(client);
        commandeAvecProduits.setPort(new BigDecimal("20.00"));
        commandeAvecProduits.setSaisiele(LocalDate.now());
        commandeAvecProduits.setRemise(new BigDecimal("0.20")); // 20% remise
        // Deux lignes dans la commande
        commandeAvecProduits.getLignes().add(new Ligne(commandeAvecProduits, chai, (short)10));
        commandeAvecProduits.getLignes().add(new Ligne(commandeAvecProduits, chang, (short)20));
        commandeDao.save(commandeAvecProduits);
    }

    @Test
    void testCommandeAvecProduits() {
        var commande = commandeDao.findById(commandeAvecProduits.getNumero()).orElseThrow();
        assertEquals(commandeAvecProduits, commande);
        assertEquals(2, commande.getLignes().size());
    }

    @Test
    void testMontantArticles() {
        // Données de test
        Integer numeroCommande = 10248; // Remplacez par une commande valide
        BigDecimal montantAttendu = new BigDecimal("2830.0000"); // Valeur attendue

        // Exécution
        BigDecimal montantCalcule = commandeDao.montantArticles(numeroCommande);

        // Vérification
        assertEquals(montantAttendu, montantCalcule);
    }

    @Test
    void testFindCommandesByClient() {
        // Données de test
        String codeClient = "ALFKI";

        // Exécution
        List<Object[]> commandes = commandeDao.findCommandesByClient(codeClient);

        // Vérification
        assertNotNull(commandes);
        assertFalse(commandes.isEmpty());
        for (Object[] commande : commandes) {
            System.out.println("Commande numéro : " + commande[0]);
            System.out.println("Saisie le : " + commande[1]);
            System.out.println("Envoyée le : " + commande[2]);
            System.out.println("Port : " + commande[3]);
            System.out.println("Destinataire : " + commande[4]);
            System.out.println("Montant total : " + commande[5]);
            System.out.println("---");
        }
    }

}