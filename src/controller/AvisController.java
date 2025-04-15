package controller;

import view.AvisPanel;
import DAO.AvisDAO;
import model.Avis;
import javax.swing.*;

publiAc class AvisController {
    private AvisPanel view;
    private AvisDAO avisDAO;

    public AvisController(AvisPanel view) {
        this.view = view;
        avisDAO = new AvisDAO();
    }

    public void submitAvis() {
        try {
            int clientId = view.getClientId();
            int hebergementId = view.getHebergementId();
            int rating = view.getRating();
            String comment = view.getComment();

            Avis newAvis = new Avis(clientId, hebergementId, rating, comment);
            if (avisDAO.insert(newAvis)) {
                JOptionPane.showMessageDialog(view, "Avis inséré avec succès !");
                view.clearFields();
            } else {
                JOptionPane.showMessageDialog(view, "Erreur lors de l'insertion de l'avis.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Veuillez vérifier les identifiants (doivent être numériques).");
        }
    }
}
