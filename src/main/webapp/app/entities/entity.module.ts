import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'famille',
        loadChildren: () => import('./famille/famille.module').then(m => m.SchoolManagementFamilleModule)
      },
      {
        path: 'eleve',
        loadChildren: () => import('./eleve/eleve.module').then(m => m.SchoolManagementEleveModule)
      },
      {
        path: 'discipline',
        loadChildren: () => import('./discipline/discipline.module').then(m => m.SchoolManagementDisciplineModule)
      },
      {
        path: 'professeur',
        loadChildren: () => import('./professeur/professeur.module').then(m => m.SchoolManagementProfesseurModule)
      },
      {
        path: 'classe',
        loadChildren: () => import('./classe/classe.module').then(m => m.SchoolManagementClasseModule)
      },
      {
        path: 'feuille-presence',
        loadChildren: () => import('./feuille-presence/feuille-presence.module').then(m => m.SchoolManagementFeuillePresenceModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class SchoolManagementEntityModule {}
