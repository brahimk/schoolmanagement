import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SchoolManagementTestModule } from '../../../test.module';
import { EleveUpdateComponent } from 'app/entities/eleve/eleve-update.component';
import { EleveService } from 'app/entities/eleve/eleve.service';
import { Eleve } from 'app/shared/model/eleve.model';

describe('Component Tests', () => {
  describe('Eleve Management Update Component', () => {
    let comp: EleveUpdateComponent;
    let fixture: ComponentFixture<EleveUpdateComponent>;
    let service: EleveService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [EleveUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EleveUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EleveUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EleveService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Eleve(123);
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
        const entity = new Eleve();
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
