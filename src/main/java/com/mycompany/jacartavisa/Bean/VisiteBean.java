package com.mycompany.jacartavisa.Bean;

import com.mycompany.jacartavisa.business.SessionManager;
import com.mycompany.jacartavisa.business.UtilisateurEntrepriseBean;
import com.mycompany.jacartavisa.business.VisiteEntrepriseBean;
import com.mycompany.jacartavisa.entities.Utilisateur;
import com.mycompany.jacartavisa.entities.Visite;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Named(value = "formulaireVisiteBean")
@RequestScoped
public class VisiteBean implements Serializable {
    @Inject
    private VisiteEntrepriseBean visiteEntrepriseBean; // Injection de l'EJB pour gérer les visites
    
    @Inject
    private SessionManager sessionManager; // Injection du SessionManager
    
    @Inject
    private UtilisateurEntrepriseBean utilisateurEntrepriseBean;

    private Long idUtilisateur;
    private Long idLieu;
    private LocalDateTime dateVisite;
    private int tempsPasse;
    private String observations;
    private double depenses;
    private boolean afficherFormulaireVisite;

    // Déclaration de l'attribut pour stocker les visites de l'utilisateur
    private List<Visite> visitesUtilisateur;

    @PostConstruct
    public void init() {
        dateVisite = LocalDateTime.now(); // Initialisation avec la date actuelle
    }

    public void afficherFormulaireVisite() {
        afficherFormulaireVisite = true;
    }

    public void sauvegarderVisite() {
        try {
            // Récupérer l'email de l'utilisateur depuis la session
            String email = sessionManager.getValueFromSession("user");
            Utilisateur user = utilisateurEntrepriseBean.trouverUtilisateurParEmail(email);
            if (user != null) {
                idUtilisateur = user.getId(); // Assigner l'ID utilisateur récupéré
            } else {
                throw new Exception("Utilisateur non trouvé dans la session.");
            }

            // Vérifier que idLieu n'est pas nul
            if (idLieu == null) {
                throw new Exception("ID Lieu ne peut pas être nul.");
            }

            Visite visite = new Visite();
            visite.setIdUtilisateur(idUtilisateur.intValue()); // Utilisation de l'ID utilisateur
            visite.setIdLieu(1); // Utilisation de l'ID lieu
            visite.setDateVisite(dateVisite);
            visite.setTempsPasse(tempsPasse);
            visite.setObservations(observations);
            visite.setDepenses(depenses);

            visiteEntrepriseBean.ajouterVisite(visite); // Ajout dans la base de données

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Visite enregistrée avec succès", null));

            afficherFormulaireVisite = false;
            resetForm();
 
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors de l'enregistrement : " + e.getMessage(), null));
            e.printStackTrace();
        }
    }

    private void resetForm() {
        idLieu = null;
        dateVisite = LocalDateTime.now();
        tempsPasse = 0;
        observations = "";
        depenses = 0.0;
    }

    // Méthode pour charger les visites de l'utilisateur
 

    // Getters et Setters
    public LocalDateTime getDateVisite() { return dateVisite; }
    public void setDateVisite(LocalDateTime dateVisite) { this.dateVisite = dateVisite; }
    public double getDepenses() { return depenses; }
    public void setDepenses(double depenses) { this.depenses = depenses; }
    public Long getIdLieu() { return idLieu; }
    public void setIdLieu(Long idLieu) { this.idLieu = idLieu; }
    public int getTempsPasse() { return tempsPasse; }
    public void setTempsPasse(int tempsPasse) { this.tempsPasse = tempsPasse; }
    public Long getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(Long idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    public void setAfficherFormulaireVisite(boolean afficherFormulaireVisite) { this.afficherFormulaireVisite = afficherFormulaireVisite; }
    public void setObservations(String observations) { this.observations = observations; }
    public String getObservations() { return observations; }

    public boolean isAfficherFormulaireVisite() {
        return afficherFormulaireVisite;
    }

    public List<Visite> getVisitesUtilisateur() {
        return visitesUtilisateur;
    }
}