import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { SchoolManagementTestModule } from '../../../test.module';
import { FeuillePresenceDetailComponent } from 'app/entities/feuille-presence/feuille-presence-detail.component';
import { FeuillePresence } from 'app/shared/model/feuille-presence.model';

describe('Component Tests', () => {
  describe('FeuillePresence Management Detail Component', () => {
    let comp: FeuillePresenceDetailComponent;
    let fixture: ComponentFixture<FeuillePresenceDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ feuillePresence: new FeuillePresence(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [FeuillePresenceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FeuillePresenceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FeuillePresenceDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load feuillePresence on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.feuillePresence).toEqual(jasmine.objectContaining({ id: 123 }));
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
