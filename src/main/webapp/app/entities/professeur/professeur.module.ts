import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SchoolManagementSharedModule } from 'app/shared/shared.module';
import { ProfesseurComponent } from './professeur.component';
import { ProfesseurDetailComponent } from './professeur-detail.component';
import { ProfesseurUpdateComponent } from './professeur-update.component';
import { ProfesseurDeleteDialogComponent } from './professeur-delete-dialog.component';
import { professeurRoute } from './professeur.route';

@NgModule({
  imports: [SchoolManagementSharedModule, RouterModule.forChild(professeurRoute)],
  declarations: [ProfesseurComponent, ProfesseurDetailComponent, ProfesseurUpdateComponent, ProfesseurDeleteDialogComponent],
  entryComponents: [ProfesseurDeleteDialogComponent]
})
export class SchoolManagementProfesseurModule {}
