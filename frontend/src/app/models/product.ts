export interface Product {
  id: number;
  name: string;
  description: string;
  imageUrl: string;
  basePrice: number;
  category: {
    id: number;
    name: string;
  };
}
