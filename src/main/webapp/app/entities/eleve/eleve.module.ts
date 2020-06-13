import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SchoolManagementSharedModule } from 'app/shared/shared.module';
import { EleveComponent } from './eleve.component';
import { EleveDetailComponent } from './eleve-detail.component';
import { EleveUpdateComponent } from './eleve-update.component';
import { EleveDeleteDialogComponent } from './eleve-delete-dialog.component';
import { eleveRoute } from './eleve.route';

@NgModule({
  imports: [SchoolManagementSharedModule, RouterModule.forChild(eleveRoute)],
  declarations: [EleveComponent, EleveDetailComponent, EleveUpdateComponent, EleveDeleteDialogComponent],
  entryComponents: [EleveDeleteDialogComponent]
})
export class SchoolManagementEleveModule {}
