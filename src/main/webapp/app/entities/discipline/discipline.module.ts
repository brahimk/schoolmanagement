import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SchoolManagementSharedModule } from 'app/shared/shared.module';
import { DisciplineComponent } from './discipline.component';
import { DisciplineDetailComponent } from './discipline-detail.component';
import { DisciplineUpdateComponent } from './discipline-update.component';
import { DisciplineDeleteDialogComponent } from './discipline-delete-dialog.component';
import { disciplineRoute } from './discipline.route';

@NgModule({
  imports: [SchoolManagementSharedModule, RouterModule.forChild(disciplineRoute)],
  declarations: [DisciplineComponent, DisciplineDetailComponent, DisciplineUpdateComponent, DisciplineDeleteDialogComponent],
  entryComponents: [DisciplineDeleteDialogComponent]
})
export class SchoolManagementDisciplineModule {}
