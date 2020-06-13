import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SchoolManagementTestModule } from '../../../test.module';
import { ProfesseurComponent } from 'app/entities/professeur/professeur.component';
import { ProfesseurService } from 'app/entities/professeur/professeur.service';
import { Professeur } from 'app/shared/model/professeur.model';

describe('Component Tests', () => {
  describe('Professeur Management Component', () => {
    let comp: ProfesseurComponent;
    let fixture: ComponentFixture<ProfesseurComponent>;
    let service: ProfesseurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [ProfesseurComponent]
      })
        .overrideTemplate(ProfesseurComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProfesseurComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProfesseurService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Professeur(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.professeurs && comp.professeurs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
