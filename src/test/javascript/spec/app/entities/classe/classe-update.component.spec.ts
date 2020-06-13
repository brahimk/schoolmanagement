import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SchoolManagementTestModule } from '../../../test.module';
import { ClasseUpdateComponent } from 'app/entities/classe/classe-update.component';
import { ClasseService } from 'app/entities/classe/classe.service';
import { Classe } from 'app/shared/model/classe.model';

describe('Component Tests', () => {
  describe('Classe Management Update Component', () => {
    let comp: ClasseUpdateComponent;
    let fixture: ComponentFixture<ClasseUpdateComponent>;
    let service: ClasseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [ClasseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ClasseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClasseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClasseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Classe(123);
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
        const entity = new Classe();
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
