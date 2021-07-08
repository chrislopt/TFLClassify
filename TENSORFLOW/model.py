import os

import numpy as np

from tensorflow.python.client import device_lib 

#assert tf.__version__.startswith('2')

from tflite_model_maker import model_spec
from tflite_model_maker import image_classifier
from tflite_model_maker.config import ExportFormat
from tflite_model_maker.config import QuantizationConfig
from tflite_model_maker.image_classifier import DataLoader

import matplotlib.pyplot as plt

print(device_lib.list_local_devices())

#Debe existir una carpeta llamada "Images" que contenga dentro mas carpetas de razas de perros y cada una de estas carpetas debe contener .jpg 
#que seran utilizadas para entrenar el modelo 
data = DataLoader.from_folder("Images")
train_data, test_data = data.split(0.9)

#El modelo se entranara con 69 epochs
model = image_classifier.create(train_data, epochs=69)

loss, accuracy = model.evaluate(test_data)

#Se especifiica que a este nivel se creara el archivo .tflite que será copiado en el proyecto de la aplicacion mobile
model.export(export_dir='.')