import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'ex-user',
        data: { pageTitle: 'ExUsers' },
        loadChildren: () => import('./ex-user/ex-user.module').then(m => m.ExUserModule),
      },
      {
        path: 'user-role',
        data: { pageTitle: 'UserRoles' },
        loadChildren: () => import('./user-role/user-role.module').then(m => m.UserRoleModule),
      },
      {
        path: 'user-permission',
        data: { pageTitle: 'UserPermissions' },
        loadChildren: () => import('./user-permission/user-permission.module').then(m => m.UserPermissionModule),
      },
      {
        path: 'company',
        data: { pageTitle: 'Companies' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'project',
        data: { pageTitle: 'Projects' },
        loadChildren: () => import('./project/project.module').then(m => m.ProjectModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'Locations' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'rating',
        data: { pageTitle: 'Ratings' },
        loadChildren: () => import('./rating/rating.module').then(m => m.RatingModule),
      },
      {
        path: 'rating-type',
        data: { pageTitle: 'RatingTypes' },
        loadChildren: () => import('./rating-type/rating-type.module').then(m => m.RatingTypeModule),
      },
      {
        path: 'certificate',
        data: { pageTitle: 'Certificates' },
        loadChildren: () => import('./certificate/certificate.module').then(m => m.CertificateModule),
      },
      {
        path: 'certificate-type',
        data: { pageTitle: 'CertificateTypes' },
        loadChildren: () => import('./certificate-type/certificate-type.module').then(m => m.CertificateTypeModule),
      },
      {
        path: 'master-item',
        data: { pageTitle: 'MasterItems' },
        loadChildren: () => import('./master-item/master-item.module').then(m => m.MasterItemModule),
      },
      {
        path: 'item',
        data: { pageTitle: 'Items' },
        loadChildren: () => import('./item/item.module').then(m => m.ItemModule),
      },
      {
        path: 'orders',
        data: { pageTitle: 'Orders' },
        loadChildren: () => import('./orders/orders.module').then(m => m.OrdersModule),
      },
      {
        path: 'order-status',
        data: { pageTitle: 'OrderStatuses' },
        loadChildren: () => import('./order-status/order-status.module').then(m => m.OrderStatusModule),
      },
      {
        path: 'order-details',
        data: { pageTitle: 'OrderDetails' },
        loadChildren: () => import('./order-details/order-details.module').then(m => m.OrderDetailsModule),
      },
      {
        path: 'unit-of-measure',
        data: { pageTitle: 'UnitOfMeasures' },
        loadChildren: () => import('./unit-of-measure/unit-of-measure.module').then(m => m.UnitOfMeasureModule),
      },
      {
        path: 'bo-qs',
        data: { pageTitle: 'BOQs' },
        loadChildren: () => import('./bo-qs/bo-qs.module').then(m => m.BOQsModule),
      },
      {
        path: 'boq-details',
        data: { pageTitle: 'BOQDetails' },
        loadChildren: () => import('./boq-details/boq-details.module').then(m => m.BOQDetailsModule),
      },
      {
        path: 'user-type',
        data: { pageTitle: 'UserTypes' },
        loadChildren: () => import('./user-type/user-type.module').then(m => m.UserTypeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
