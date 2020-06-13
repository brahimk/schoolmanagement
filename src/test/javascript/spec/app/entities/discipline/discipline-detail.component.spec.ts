import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { SchoolManagementTestModule } from '../../../test.module';
import { DisciplineDetailComponent } from 'app/entities/discipline/discipline-detail.component';
import { Discipline } from 'app/shared/model/discipline.model';

describe('Component Tests', () => {
  describe('Discipline Management Detail Component', () => {
    let comp: DisciplineDetailComponent;
    let fixture: ComponentFixture<DisciplineDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ discipline: new Discipline(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [DisciplineDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DisciplineDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DisciplineDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load discipline on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.discipline).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
