import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SchoolManagementSharedModule } from 'app/shared/shared.module';
import { FeuillePresenceComponent } from './feuille-presence.component';
import { FeuillePresenceDetailComponent } from './feuille-presence-detail.component';
import { FeuillePresenceUpdateComponent } from './feuille-presence-update.component';
import { FeuillePresenceDeleteDialogComponent } from './feuille-presence-delete-dialog.component';
import { feuillePresenceRoute } from './feuille-presence.route';

@NgModule({
  imports: [SchoolManagementSharedModule, RouterModule.forChild(feuillePresenceRoute)],
  declarations: [
    FeuillePresenceComponent,
    FeuillePresenceDetailComponent,
    FeuillePresenceUpdateComponent,
    FeuillePresenceDeleteDialogComponent
  ],
  entryComponents: [FeuillePresenceDeleteDialogComponent]
})
export class SchoolManagementFeuillePresenceModule {}
