import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SchoolManagementTestModule } from '../../../test.module';
import { FeuillePresenceUpdateComponent } from 'app/entities/feuille-presence/feuille-presence-update.component';
import { FeuillePresenceService } from 'app/entities/feuille-presence/feuille-presence.service';
import { FeuillePresence } from 'app/shared/model/feuille-presence.model';

describe('Component Tests', () => {
  describe('FeuillePresence Management Update Component', () => {
    let comp: FeuillePresenceUpdateComponent;
    let fixture: ComponentFixture<FeuillePresenceUpdateComponent>;
    let service: FeuillePresenceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [FeuillePresenceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FeuillePresenceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FeuillePresenceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FeuillePresenceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FeuillePresence(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new FeuillePresence();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
