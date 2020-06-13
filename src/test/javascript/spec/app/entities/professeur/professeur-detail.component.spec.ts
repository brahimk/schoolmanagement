import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SchoolManagementTestModule } from '../../../test.module';
import { ProfesseurDetailComponent } from 'app/entities/professeur/professeur-detail.component';
import { Professeur } from 'app/shared/model/professeur.model';

describe('Component Tests', () => {
  describe('Professeur Management Detail Component', () => {
    let comp: ProfesseurDetailComponent;
    let fixture: ComponentFixture<ProfesseurDetailComponent>;
    const route = ({ data: of({ professeur: new Professeur(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [ProfesseurDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ProfesseurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProfesseurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load professeur on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.professeur).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
