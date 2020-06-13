import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SchoolManagementTestModule } from '../../../test.module';
import { ProfesseurUpdateComponent } from 'app/entities/professeur/professeur-update.component';
import { ProfesseurService } from 'app/entities/professeur/professeur.service';
import { Professeur } from 'app/shared/model/professeur.model';

describe('Component Tests', () => {
  describe('Professeur Management Update Component', () => {
    let comp: ProfesseurUpdateComponent;
    let fixture: ComponentFixture<ProfesseurUpdateComponent>;
    let service: ProfesseurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [ProfesseurUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ProfesseurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProfesseurUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProfesseurService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Professeur(123);
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
        const entity = new Professeur();
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
