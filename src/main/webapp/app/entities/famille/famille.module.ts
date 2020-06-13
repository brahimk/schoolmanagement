import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SchoolManagementSharedModule } from 'app/shared/shared.module';
import { FamilleComponent } from './famille.component';
import { FamilleDetailComponent } from './famille-detail.component';
import { FamilleUpdateComponent } from './famille-update.component';
import { FamilleDeleteDialogComponent } from './famille-delete-dialog.component';
import { familleRoute } from './famille.route';

@NgModule({
  imports: [SchoolManagementSharedModule, RouterModule.forChild(familleRoute)],
  declarations: [FamilleComponent, FamilleDetailComponent, FamilleUpdateComponent, FamilleDeleteDialogComponent],
  entryComponents: [FamilleDeleteDialogComponent]
})
export class SchoolManagementFamilleModule {}
